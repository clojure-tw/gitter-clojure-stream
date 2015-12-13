(ns gitter-clojure-stream.core
  {:author "Yen-Chin, Lee"
   :doc "Gitter Stream API example client for Clojure"}
  (:require [http.async.client :as http]
            [clojure.data.json :as json]))

(def api-key (System/getenv "TOKEN"))
(def room-id (System/getenv "ROOM_ID"))

(defn -main [& args]
  (with-open [conn (http/create-client)]
    (let [resp (http/stream-seq conn
                                :get (str "https://stream.gitter.im/v1/rooms/" room-id "/chatMessages")
                                :headers {"Authorization" (str "Bearer " api-key) "Connection" "keep-alive"}
                                :timeout -1)]
      (doseq [s (http/string resp)]
        (when-not (clojure.string/blank? s)
          (println (str (json/read-str s :key-fn keyword)))
          )))))
