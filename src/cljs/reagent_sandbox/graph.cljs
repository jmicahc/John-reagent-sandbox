(ns reagent-sandbox.graph
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]
                   [cljs.pprint :refer [pp]])
  (:require [reagent.core :as reagent]
            [reagent-sandbox.data :as data
             :refer [mutate-chan debounced-mutate
                     debounce-interval]]
            [cljs.pprint :refer [pprint]]
            [cljs.core.async :as async :refer
             [mult timeout put! <! >! pipe]]))


(declare rx)


(defn cartesian-product [node]
  "f: List X List X List --> List X List
     Computes cartesian cross product of list of lists of lists of elements.
     (((a b) (c d)) ((e f) (g h))) ==> ((a b e f) (a b g h) (c d e f) (c d g h))"
  (reduce (fn [left right]
            (mapcat (fn [lhs] (map #(concat lhs %) right)) left))
          (first node)
          (next node)))



(defn tree->width-equations
  ([] [[[]]])
  ([{:keys [magnitude partition term] :as node}]
   (if (= partition :column)
     (if magnitude
       (if (= term :var) (list (list node)) [[]])
       (fn [c] (into [] (comp (remove #(= % [[]])) cat) c)))
     cartesian-product)))



(defn tree->height-equations
  ([] [[[]]])
  ([{:keys [magnitude partition term] :as node}]
   (if (= partition :row)
     (if magnitude
       (if (= term :var) (list (list node)) [[]])
       (fn [c] (into [] (comp (remove #(= % [[]])) cat) c)))
     cartesian-product)))


(defn solve-equation
  [eq dx]
  (let [freq (count eq)
        x    (/ dx freq)]
    (mapv (fn [term] (update term :magnitude + x)) eq)))


(defn solve-equations
  [eqs dx]
  (mapv (fn [eq] (solve-equation eq dx)) eqs))


(defn equation->tree
  [[{:keys [type id] :as term} & terms] g]
  (if term
    (recur terms (assoc-in g [type id] term))
    g))


(defn equations->tree
  [[eq & eqs] state]
  (if-not eq
    state
    (recur eqs (equation->tree eq state))))


(defn update-row
  [g node width]
  (->
   (rx g node tree->width-equations)
   (solve-equations width)
   (equations->tree g)))


(defn update-column
  [g node height]
  (->
   (rx g node tree->height-equations)
   (solve-equations height)
   (equations->tree g)))


(defn update-grid
  [g node width height]
  (-> (update-row g node width)
      (update-column node height)))


(defn default-nav [g node]
  (map #(get-in g %) (:children node)))


(defn rx
  ([node node-fn]
   (rx @data/graph node node-fn default-nav))
  ([g node node-fn]
   (rx g node node-fn default-nav))
  ([g node node-fn nav-fn]
   (letfn [(build [node]
             (let [x  (node-fn node)
                   xs (nav-fn g node)]
               (if (fn? x)
                 (if (empty? xs) (x (node-fn))
                     (x (map (fn [n] (build n)) xs)))
                 x)))]
     (build node))))

(defn window-width [] (.-innerWidth js/window))
(defn window-height [] (.-innerHeight js/window))

(defn start-mutate-chan [ratom]
  (go-loop []
    (let [m (<! mutate-chan)]
      (let [ident [:layout/root 1]
            root (get-in @ratom ident)
            w (:width root)
            h (:height root)
            win-width (window-width)
            win-height (window-height)]
        (swap! ratom update-in ident
               assoc
               :width  win-width
               :height win-height)
        (swap! ratom update-grid
               root (- win-width w) (- win-height h)))
      (recur))))
