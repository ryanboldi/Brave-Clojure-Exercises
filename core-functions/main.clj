(ns main)

(defn -main
  [& args]
  (println "Hello world!"))

;Implement map using reduce
(defn my_map
  "implements the map function using reduce"
  [func sequence]
  (seq (reduce (fn [done left]
                 (into done [(func left)]))
               []
               (seq sequence))))

(my_map inc [1 2 3]) ; => (2 3 4)

;implement filter using reduce
(defn my_filter
  "implements the filter function using reduce"
  [predicate_func sequence]
  (seq (reduce (fn [good left]
                 (if (predicate_func left)
                   (into good [left])
                   good))
               []
               (seq sequence))))

(my_filter #(> % 3) [1 2 9 3 10 54]) ; => (9 10 54)

;implement some using reduce
(defn my_some
  "implements some using reduce"
  [predicate_func sequence]
  (some identity (reduce (fn [good left]
                           (into good [(predicate_func left)]))
                         []
                         (seq sequence))))

(my_some #(> % 3) [1 2 9 3 10 54]) ; => true
(my_some #(and (> % 3) %) [1 2 9 3 10 54]) ; => 9


(def suspect_file "suspects.csv")
(slurp suspect_file)

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\r\n")))

(parse (slurp suspect_file))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))


(first (mapify (parse (slurp suspect_file))))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(glitter-filter 3 (mapify (parse (slurp suspect_file))))

;Exercise 1 

(defn glitter-filter-namelist
  [minimum-glitter records]
  (map #(:name %) (filter #(>= (:glitter-index %) minimum-glitter) records)))

(glitter-filter-namelist 3 (mapify (parse (slurp suspect_file))))

;Exercise 2

(defn append-to-suspectlist
  [suspect-name suspect-glitter list-of-maps]
  (into list-of-maps (assoc {} :name suspect-name :glitter-index suspect-glitter)))

(append-to-suspectlist "Ryan Boldi" "45" (mapify (parse (slurp suspect_file))))

;Exercise 3

(def validation-map {:name identity, :glitter-index identity})
