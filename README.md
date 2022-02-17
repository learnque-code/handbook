# handbook
Application for serving guides, slides and so on
___

This project depends on `mdbook` binaries. You need to correctly configure it so it would be possible to compile guides from md into html.

Basically, you need specify path to `mdbook` executable file in configuration file:

```yaml
...
    web:
      bin: ${HANDBOOK_HOME}/bin
...
```