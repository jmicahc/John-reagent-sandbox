(ns reagent-sandbox.data
  (:require [reagent.core :as reagent]
            [cljs.core.async :as async :refer
             [chan dropping-buffer]]))


(def mutate-chan (chan (dropping-buffer 100)))
(def debounced-mutate (chan (dropping-buffer 1)))
(def debounce-interval 100)




(def graph
  (reagent/atom
   {:layout/root
    {1
     {:type :layout/root
      :partition :column
      :id 1
      :width  500
      :height 700
      :top 0
      :left 0
      :children [[:layout/root 13]
                 [:layout/row 2]
                 [:layout/row 3]
                 [:style/root 7]]}
     13
     {:type :layout/root
      :partition :column
      :magnitude 400
      :id 13
      :width 400
      :height 700
      :left 300
      :tpo 500
      :children [[:style/root 14]
                 [:layout/row 15]
                 [:layout/row 16]]}}
    :layout/row
    {2
     {:type :layout/row
      :partition :row
      :id 2
      :magnitude 100
      :children [[:style/root 8]
                 [:layout/column 4]
                 [:layout/column 5]]}
     3
     {:type :layout/row
      :partition :row
      :magnitude 600
      :id 3
      :term :var
      :children [[:modify/conjoin 12]
                 [:style/root 9]
                 [:event/root 6]]}
     15
     {:type :layout/row
      :parittion :row
      :id 15
      :magnitude 500
      :children  [[:style/root 17]]}
     16
     {:type :layout/row
      :partition :row
      :id 16
      :magnitude 200
      :children [[:style/root 18]]}}

    :layout/column
    {4
     {:type :layout/column
      :partition :column
      :id 4
      :magnitude 200
      :children [[:style/root 10]]}
     5
     {:type :layout/column
      :partition :column
      :id 5
      :magnitude 300
      :term :var
      :children [[:style/root 11]]}}
    :event/root
    {6
     {:type :event/root
      :id 6}}
    :style/root
    {7
     {:type :style/root
      :id 7
      :backgroundColor "orange"}
     8
     {:type :style/root
      :id 8
      :backgroundColor "green"}
     9
     {:type :style/root
      :id 9
      :backgroundColor "orange"}
     10
     {:type :style/root
      :id 10
      :backgroundColor "blue"}
     11
     {:type :style/root
      :id 11
      :backgroundColor "grey"}
     14
     {:type :style/root
      :id 14
      :backgroundColor "red"}
     17
     {::type :style/root
      :backgroundColor "black"}
     18
     {:type :style/root
      :id 18
      :backgroundColor "green"}}
    :product
    {19
     {:type :product
      :operands [[:layout/root 1]
                 [:product 20]]}
     20
     {:type :product
      :operands [[:layout/root 13]
                 [:layout/row 2]
                 [:layout/row 3]
                 [:style/root 7]]}}}))


