(ns reagent-sandbox.layout
  (:require [reagent.core :as reagent]
            [reagent-sandbox.event :as event]
            [reagent-sandbox.style :as style]))



(defn layout-column
  [{:keys [id magnitude]}]
  [:div {:key id
         :style
         {:width magnitude
          :display "flex"
          :flexDirection "column"}}])


(defn layout-row
  [{:keys [id magnitude]}]
  [:div {:key id
         :style
         {:height magnitude
          :display "flex"
          :flexDirection "row"}}])


(defn layout-root
  [{:keys [id width height top left partition]}]  
  [:div {:key id
         :style
         {:width width
          :height height
          :top top
          :left left
          :display "flex"
          :position "absolute"
          :flexDirection partition}}])
