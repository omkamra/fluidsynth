(ns omkamra.fluidsynth.api
  (:require [omkamra.jnr.library :as library]))

(library/define $fl
  "fluidsynth"
  (^String fluid_version_str [])

  (^Pointer new_fluid_settings [])
  (^void delete_fluid_settings [^Pointer settings])

  (^int fluid_settings_getnum [^Pointer settings
                               ^String name
                               ^double* val])
  (^int fluid_settings_setnum [^Pointer settings
                               ^String name
                               ^double val])
  (^int fluid_settings_getint [^Pointer settings
                               ^String name
                               ^int* val])
  (^int fluid_settings_setint [^Pointer settings
                               ^String name
                               ^int val])
  (^int fluid_settings_copystr [^Pointer settings
                                ^String name
                                ^Pointer str
                                ^int len])
  (^int fluid_settings_setstr [^Pointer settings
                               ^String name
                               ^String val])

  (^Pointer new_fluid_synth (^Pointer settings))
  (^void delete_fluid_synth (^Pointer synth))

  (^Pointer fluid_synth_get_settings (^Pointer synth))
  (^int fluid_synth_noteon (^Pointer synth
                            ^int chan
                            ^int key
                            ^int vel))
  (^int fluid_synth_noteoff (^Pointer synth
                             ^int chan
                             ^int key))
  (^int fluid_synth_cc (^Pointer synth
                        ^int chan
                        ^int ctrl
                        ^int val))
  (^int fluid_synth_pitch_bend (^Pointer synth
                                ^int chan
                                ^int val))
  (^int fluid_synth_program_change (^Pointer synth
                                    ^int chan
                                    ^int program))
  (^int fluid_synth_bank_select (^Pointer synth
                                 ^int chan
                                 ^int bank))
  (^int fluid_synth_sfont_select (^Pointer synth
                                  ^int chan
                                  ^int sfont_id))
  (^int fluid_synth_program_select (^Pointer synth
                                    ^int chan
                                    ^int sfont_id
                                    ^int bank_num
                                    ^int preset_num))
  (^int fluid_synth_program_reset (^Pointer synth))
  (^int fluid_synth_system_reset (^Pointer synth))
  (^int fluid_synth_all_notes_off (^Pointer synth
                                   ^int chan))
  (^int fluid_synth_all_sounds_off (^Pointer synth
                                    ^int chan))
  (^int fluid_synth_set_channel_type (^Pointer synth
                                      ^int chan
                                      ^int type))
  (^int fluid_synth_sfload (^Pointer synth
                            ^String filename
                            ^int reset_presets))
  (^int fluid_synth_sfreload (^Pointer synth
                              ^int id))
  (^int fluid_synth_sfunload (^Pointer synth
                              ^int id
                              ^int reset_presets))
  (^int fluid_synth_add_sfont (^Pointer synth
                               ^Pointer sfont))
  (^int fluid_synth_remove_sfont (^Pointer synth
                                  ^Pointer sfont))
  (^int fluid_synth_sfcount (^Pointer synth))
  (^int fluid_synth_set_bank_offset (^Pointer synth
                                     ^int sfont_id
                                     ^int offset))
  (^double fluid_synth_get_cpu_load (^Pointer synth))

  (^Pointer new_fluid_audio_driver (^Pointer settings
                                    ^Pointer synth))
  (^void delete_fluid_audio_driver (^Pointer driver)))
