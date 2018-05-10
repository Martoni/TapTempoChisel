TapTempo
========

A Chisel version of [TapTempo project](https://github.com/moleculext/taptempo.git).
TapTempo been ported in severals langages, can be found on [LinuxFR website](https://linuxfr.org/tags/taptempo/public)

### Launch test

With sbt :
```sh
sbt 'test:runMain taptempo.TapTempoMain --backend-name verilator'
```

### Generate bitstream

## For APF27

First generate verilog:
```sh
sbt runMain taptempo.APF27TapTempoDriver
```

Then add file to ise project
