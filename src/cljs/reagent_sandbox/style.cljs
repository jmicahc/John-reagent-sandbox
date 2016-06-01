(ns reagent-sandbox.style
  (:require [reagent.core :as reagent]))


(defn style [{:keys [backgroundColor]}]
  {:style {:backgroundColor backgroundColor}})
