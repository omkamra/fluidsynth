# omkamra.fluidsynth

A Clojure wrapper around the [FluidSynth](https://www.fluidsynth.org/)
[SoundFont](https://github.com/FluidSynth/fluidsynth/wiki/SoundFont)
synthesizer library.

## Usage

Play a major scale starting at MIDI note 60 using the `Grand Piano`
patch of the
[FluidR3](https://packages.debian.org/sid/fluid-soundfont-gm) General
MIDI SoundFont:

```clojure
(ns com.example.fluidsynth
  (:require [omkamra.fluidsynth.settings :as settings]
            [omkamra.fluidsynth.synth :as synth]
            [omkamra.fluidsynth.audio-driver :as audio-driver]))

(let [settings (settings/create)
      synth (synth/create settings)
      audio-driver (audio-driver/create synth)]

  (try
    (synth/sfload synth "/usr/share/soundfonts/FluidR3_GM.sf2")

    (let [channel 0
          program 0
          velocity 100]
      (synth/program-change synth channel program)
      (doseq [interval [0 2 4 5 7 9 11 12]]
        (let [note (+ 60 interval)]
          (synth/noteon synth channel note velocity)
          (Thread/sleep 1000)
          (synth/noteoff synth channel note))))

    (finally
      (audio-driver/delete audio-driver)
      (synth/delete synth)
      (settings/delete settings))))
```

### Settings

These are the built-in defaults:

```clojure
{:synth
 {:gain 1.0
  :sample-rate 48000.0
  :midi-channels 256}
 :audio
 {:driver "pulseaudio"
  :period-size 1024}}
```

To add your own
[settings](https://www.fluidsynth.org/api/fluidsettings.html), pass a
config map like the above to `settings/create`.

To see how that config map should look like, what kind of settings are
available and how they are currently configured, pretty print the
result of the following expression while `synth` is active:

```clojure
(settings/decode (synth/get-settings synth))
```

## Limitations

- provides only a small subset of the C API (just the bare minimum to make music)
- no error handling
- no tests

## License

Copyright © 2021 Balázs Ruzsa

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
