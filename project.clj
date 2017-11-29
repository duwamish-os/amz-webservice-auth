(defproject aws-creds "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 ;;[com.specs/specs-lib "1.0"]
                 ;;[com.IBM.AS400/DB2DriverAS400 "1.0.0"]
                 [clj-http "3.6.1"]
                 [org.clojure/data.json "0.2.6"]
                 [cheshire "5.8.0"]
                 [commons-dbcp/commons-dbcp "1.2.2"]]

:user {:repository [["clojars" "http://clojars.org/repo/"]
               ;;["sonatype" "https://oss.sonatype.org/content/repositories/releases/"]
               ["mvncentral" "http://central.maven.org/maven2/"]]}

  :main ^:skip-aot aws-creds.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
