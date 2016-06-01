(ns reagent-sandbox.macros)

(defmacro component [name params & r]
  `(defn ~name ~params
     (cljs.core/js-obj "name" ~(keyword (clojure.core/name name)) ~@r)))
