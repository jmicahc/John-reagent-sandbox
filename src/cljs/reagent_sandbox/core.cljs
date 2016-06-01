(ns reagent-sandbox.core
  (:require [reagent.core :as reagent]
            [reagent-sandbox.layout :as layout]
            [reagent-sandbox.style :as style]
            [reagent-sandbox.event :as event]
            [reagent-sandbox.data :as data]
            [reagent-sandbox.graph :as graph]
            [cljs.pprint :refer [pprint]]))


(enable-console-print!)


(defn g-merge [htmlm pm]
  (let [m (merge htmlm (:attrs pm))]
    (if-let [style (:style pm)]
      (update m :style merge style)
      m)))


(defn g-combine [parent child]
  (cond
    (nil? child)
    parent

    (and (map? parent) (map? child))
    (->
     (update parent :attrs merge (:attrs child))
     (update :style merge (:style child)))

    (map? child)
    (update parent 1 g-merge child)

    :else
    (conj parent child)))


(defn render
  ([] nil)
  ([{:keys [type] :as props}]
   (fn [children]
     (let [node ((case type
                   :layout/root   layout/layout-root
                   :layout/row    layout/layout-row
                   :layout/column layout/layout-column
                   :style/root    style/style
                   :event/root    event/event
                   (fn [p] [:div props])) props)]
       (loop [children children
              result node]
         (if children
           (recur
            (next children)
            (g-combine result (first children)))
           result))))))


(defn dispatch [g root]
  (graph/rx @g root render))


(defn root-component [ratom]
  (fn []
    (do
      (print "starting mutate-chan")
      (graph/start-mutate-chan ratom))
    [:div {:id "root-elem"}
     (dispatch ratom (get-in @ratom [:layout/root 1]))]))


(defn reload []
  (reagent/render [root-component data/graph]
                  (.getElementById js/document "app")))


(defn ^:export main []
  (reload))
