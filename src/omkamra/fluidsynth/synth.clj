(ns omkamra.fluidsynth.synth
  (:require [omkamra.fluidsynth.api :refer [$fl]]
            [omkamra.fluidsynth.settings :as settings]))

(defn create
  ([settings]
   (if (map? settings)
     (create (settings/create settings))
     (.new_fluid_synth $fl settings)))
  ([]
   (create settings/default-settings)))

(defn delete
  [&synth]
  (.delete_fluid_synth $fl &synth))

(defn get-settings
  [&synth]
  (.fluid_synth_get_settings $fl &synth))

(defn noteon
  [&synth chan key vel]
  (.fluid_synth_noteon $fl &synth chan key vel))

(defn noteoff
  [&synth chan key]
  (.fluid_synth_noteoff $fl &synth chan key))

(defn program-change
  [&synth chan program]
  (.fluid_synth_program_change $fl &synth chan program))

(defn bank-select
  [&synth chan bank]
  (.fluid_synth_bank_select $fl &synth chan bank))

(defn sfont-select
  [&synth chan sfont-id]
  (.fluid_synth_sfont_select $fl &synth chan sfont-id))

(defn program-select
  [&synth chan sfont-id bank-num preset-num]
  (.fluid_synth_program_select $fl &synth chan sfont-id
                               bank-num preset-num))

(defn all-notes-off
  [&synth chan]
  (.fluid_synth_all_notes_off $fl &synth chan))

(defn all-sounds-off
  [&synth chan]
  (.fluid_synth_all_sounds_off $fl &synth chan))

(defn set-channel-type
  [&synth chan type]
  (.fluid_synth_set_channel_type $fl &synth chan type))

(defn sfload
  ([&synth filename]
   (sfload &synth filename 1))
  ([&synth filename reset-presets]
   (.fluid_synth_sfload $fl &synth filename reset-presets)))

(defn sfreload
  [&synth id]
  (.fluid_synth_sfreload $fl &synth id))

(defn sfunload
  ([&synth id]
   (.fluid_synth_sfunload $fl &synth id 1))
  ([&synth id reset-presets]
   (.fluid_synth_sfunload $fl &synth id reset-presets)))

(defn get-cpu-load
  [&synth]
  (.fluid_synth_get_cpu_load $fl &synth))
