(ns aws-creds.v2-specs
  (:require [clojure.test :refer :all]
            [aws-creds.core :refer :all]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.data.json :as json2]
            ))

(deftest orders
  (testing "orders"
    (let [response (http/post "https://shaharma.com/online/token"
                              {:headers {"Content-Type" "application/json"}
                               :accept :json
                               :body "{\"username\": \"abc@shaharma.com\", \"password\": \"1111\", \"grant_type\":\"password\"}"})]
      ;;(println (str "[INFO] auth http response " response ))
      (is (= (:status response) 200))
      (let [body (json/parse-string (get-in response [:body]) true)
            access-token (body :access_token)
            user-id (body :entities)
            uuid ((user-id 0) :uuid)]
        (println uuid)
        )
      )))


