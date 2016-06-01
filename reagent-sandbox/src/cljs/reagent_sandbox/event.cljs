(ns reagent-sandbox.event
  (:require [reagent.core :as reagent]
            [reagent-sandbox.graph :as graph :refer [rx]]
            [reagent-sandbox.data :as data
             :refer [mutate-chan debounced-mutate]]
            [cljs.core.async :refer [put!]]))

(defn event [node]
  {:attrs {:onClick (fn [e]
                      (put! mutate-chan "string")
                      :not-false)}})






