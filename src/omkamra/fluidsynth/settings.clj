(ns omkamra.fluidsynth.settings
  (:require
   [clojure.string :as str]
   [omkamra.jnr.library :as library]
   [omkamra.fluidsynth.api :refer [$fl]]))

(defn setting-type
  [value]
  (cond
    (boolean? value) :bool
    (integer? value) :int
    (float? value) :num
    (string? value) :str
    (map? value) :map
    :else (throw (ex-info
                  "setting value with unhandled type"
                  {:value value}))))

(defmulti apply-setting
  (fn [&settings key value]
    (setting-type value)))

(defmethod apply-setting :bool
  [&settings key value]
  (.fluid_settings_setint $fl &settings key (if value 1 0))
  &settings)

(defmethod apply-setting :int
  [&settings key value]
  (.fluid_settings_setint $fl &settings key value)
  &settings)

(defmethod apply-setting :num
  [&settings key value]
  (.fluid_settings_setnum $fl &settings key value)
  &settings)

(defmethod apply-setting :str
  [&settings key value]
  (.fluid_settings_setstr $fl &settings key value)
  &settings)

(defmethod apply-setting :map
  [&settings key value]
  (reduce
   (fn [&settings [k v]]
     (apply-setting &settings
                    (str key \. (name k))
                    v))
   &settings value))

(def known-settings
  "http://www.fluidsynth.org/api/fluidsettings.xml"
  {:audio
   {:driver :str
    :periods :int
    :period-size :int
    :realtime-prio :int
    :sample-format :str
    :alsa
    {:device :str}
    :coreaudio
    {:device :str}
    :dart
    {:device :str}
    :dsound
    {:device :str}
    :file
    {:endian :str
     :format :str
     :name :str
     :type :str}
    :jack
    {:autoconnect :bool
     :id :str
     :multi :bool
     :server :str}
    :oboe
    {:id :int
     :sharing-mode :str
     :performance-mode :str}
    :oss
    {:device :str}
    :portaudio
    {:device :str}
    :pulseaudio
    {:adjust-latency :bool
     :device :str
     :media-role :str
     :server :str}}
   :midi
   {:autoconnect :bool
    :driver :str
    :realtime-prio :int
    :portname :str
    :alsa
    {:device :str}
    :alsa_seq
    {:device :str
     :id :str}
    :coremidi
    {:id :str}
    :jack
    {:server :str
     :id :str}
    :oss
    {:device :str}
    :winmidi
    {:device :str}}
   :player
   {:reset-synth :bool
    :timing-source :str}
   :shell
   {:prompt :str
    :port :num}
   :synth
   {:audio-channels :int
    :audio-groups :int
    :chorus
    {:active :bool
     :depth :num
     :level :num
     :nr :int
     :speed :num}
    :cpu-cores :int
    :default-soundfont :str
    :device-id :int
    :dynamic-sample-loading :bool
    :effects-channels :int
    :effects-groups :int
    :gain :num
    :ladspa
    {:active :bool}
    :lock-memory :bool
    :midi-channels :int
    :midi-bank-select :str
    :min-note-length :int
    :overflow
    {:age :num
     :important :num
     :important-channels :str
     :percussion :num
     :released :num
     :sustained :num
     :volume :num}
    :polyphony :int
    :reverb
    {:active :bool
     :damp :num
     :level :num
     :room-size :num
     :width :num}
    :sample-rate :num
    :threadsafe-api :bool
    :verbose :bool}})

(def default-settings
  {:synth
   {:gain 1.0
    :sample-rate 48000.0
    :midi-channels 256}
   :audio
   {:driver "pulseaudio"
    :period-size 1024}})

(defn deep-merge
  "Recursively merges maps."
  [& maps]
  (letfn [(m [to from]
            (if (and (map? from) (not (record? from)))
              (merge-with m to from)
              from))]
    (reduce m maps)))

(defn create
  ([settings]
   (reduce (fn [&settings [k v]]
             (apply-setting &settings (name k) v))
           (.new_fluid_settings $fl)
           (deep-merge default-settings settings)))
  ([]
   (create {})))

(defn delete
  [&settings]
  (.delete_fluid_settings $fl &settings))

(defmulti decode-setting
  (fn [&settings key value-type] value-type))

(defn ok?
  [x]
  (zero? x))

(defmethod decode-setting :bool
  [&settings key _]
  (let [&int (jnr.ffi.byref.IntByReference.)]
    (when (ok? (.fluid_settings_getint $fl &settings key &int))
      (pos? (.getValue &int)))))

(defmethod decode-setting :int
  [&settings key _]
  (let [&int (jnr.ffi.byref.IntByReference.)]
    (when (ok? (.fluid_settings_getint $fl &settings key &int))
      (.getValue &int))))

(defmethod decode-setting :num
  [&settings key _]
  (let [&double (jnr.ffi.byref.DoubleByReference.)]
    (when (ok? (.fluid_settings_getnum $fl &settings key &double))
      (.getValue &double))))

(defmethod decode-setting :str
  [&settings key _]
  (let [bufsize 1024
        &str (jnr.ffi.Pointer/wrap (library/runtime $fl)
                                   (java.nio.ByteBuffer/allocate bufsize))]
    (when (ok? (.fluid_settings_copystr $fl &settings key &str bufsize))
      (.getString &str 0))))

(defn decode
  ([&settings]
   (decode &settings known-settings []))
  ([&settings known-settings parents]
   (reduce (fn [settings [k v]]
             (assoc settings k
                    (if (map? v)
                      (decode &settings
                              v
                              (conj parents k))
                      (decode-setting &settings
                                      (->> (conj parents k)
                                           (map name)
                                           (str/join "."))
                                      v))))
           {} known-settings)))
