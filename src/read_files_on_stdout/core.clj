(ns read-files-on-stdout.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.pprint :as cpp]
            [doric.core :refer [table]])
  (:import [java.io BufferedReader StringReader]))


(def path (atom ""))

(defn get-files-dirs-from-path
  "Return all the files and directory names from given dir-path"
  [dir-path]
  (file-seq (io/file dir-path)))

(defn only-files
  "Return only file's object"
  [files-seq]
  (filter #(.isFile %) files-seq))

(defn file-names
  "Return only file's name"
  [files-seq]
  (map #(.getName %) files-seq))

(defn seq-file-name
  "Return only files name with lexical order"
  [dir-path]
  (take 5 (sort (file-names (only-files (get-files-dirs-from-path dir-path))))))

(defn get-file-contents
  "Return contents inside perticular file"
  [dir-path file-name]
  (with-open [rdr (io/reader (str dir-path "/" file-name)
                             :append true
                             :encoding "UTF-8")]
    (doall (line-seq rdr))))

(defn file-name-wo-ex [seq]
  (mapv (fn [file] (first (string/split file #"[.]"))) seq))

;; storing map of file-names and it's contents in vector
(defn data-map [dir-path]
  (loop [return-data (sorted-map)
         files       (seq-file-name dir-path)
         file-k      (file-name-wo-ex files)]
    (if (empty? files)
      return-data
      (recur (assoc return-data
                    (keyword (first file-k))
                    (into [] (get-file-contents dir-path (first files))))
             (rest files)
             (rest file-k)))))

(defn table-header [dir-path]
  (loop [return-vec []
         file-name  (file-name-wo-ex (seq-file-name dir-path))]
    (if (empty? file-name)
      return-vec
      (recur (conj return-vec (assoc {}
                                     :name (keyword (first file-name))
                                     :align :left
                                     :width 25))
             (rest file-name)))))

(defn content-format-map [files-name]
  (loop [return-map (sorted-map)
         f-n        files-name]
    (if (empty? f-n)
      return-map
      (recur (assoc return-map (keyword (first f-n)) nil)
             (rest f-n)))))

(defn content-maps [dir-path]
  (let [f-name (seq-file-name dir-path)
        f1     (get-file-contents dir-path (first f-name))
        f2     (get-file-contents dir-path (second f-name))
        f3     (get-file-contents dir-path (second (rest f-name)))
        f4     (get-file-contents dir-path (second (rest (rest f-name))))
        f5     (get-file-contents dir-path (second (rest (rest (rest f-name)))))
        mp     (content-format-map (file-name-wo-ex (seq-file-name dir-path)))
        ky     (keys mp)
        k1     (first ky)
        k2     (second ky)
        k3     (second (rest ky))
        k4     (second (rest (rest ky)))
        k5     (second (rest (rest (rest ky))))]
    (loop [return-vec []
           fn1        f1
           fn11       (first fn1)
           fn2        f2
           fn22       (first fn2)
           fn3        f3
           fn33       (first fn3)
           fn4        f4
           fn44       (first fn4)
           fn5        f5
           fn55       (first fn5)]
      (if (every? true? [(empty? fn1)
                         (empty? fn2)
                         (empty? fn11)
                         (empty? fn22)
                         (empty? fn3)
                         (empty? fn33)
                         (empty? fn4)
                         (empty? fn44)
                         (empty? fn5)
                         (empty? fn55)])
        return-vec
        (recur (conj return-vec
                     (assoc mp
                            k1 (apply str (take 25 fn11))
                            k2 (apply str (take 25 fn22))
                            k3 (apply str (take 25 fn33))
                            k4 (apply str (take 25 fn44))
                            k5 (apply str (take 25 fn55))))
               (if (empty? fn11)
                 (rest fn1)
                 fn1)
               (if (empty? fn11)
                 (first (rest fn1))
                 (apply str (drop 25 fn11)))
               (if (empty? fn22)
                 (rest fn2)
                 fn2)
               (if (empty? fn22)
                 (first (rest fn2))
                 (apply str (drop 25 fn22)))
               (if (empty? fn33)
                 (rest fn3)
                 fn3)
               (if (empty? fn33)
                 (first (rest fn3))
                 (apply str (drop 25 fn33)))
               (if (empty? fn44)
                 (rest fn4)
                 fn4)
               (if (empty? fn44)
                 (first (rest fn4))
                 (apply str (drop 25 fn44)))
               (if (empty? fn55)
                 (rest fn5)
                 fn5)
               (if (empty? fn55)
                 (first (rest fn5))
                 (apply str (drop 25 fn55)))
               )))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (reset! path (first args))
  (println (str "Your input path is : " @path))
  #_(cpp/pprint (content-maps @path))
  (println (table (table-header @path)
                  (content-maps @path))))
