# errorlens

![Build](https://github.com/bahamondev/errorlens/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/17622-errorlens.svg)](https://plugins.jetbrains.com/plugin/17622-errorlens)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/17622-errorlens.svg)](https://plugins.jetbrains.com/plugin/17622-errorlens)

<!-- Plugin description -->
Errorlens is a plugin for the Intellij-based IDEs which enhances the error diagnostics.
With this plugin, error descriptions are shown inline in the editor, in the same line which generates the error.
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "errorlens"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/bahamondev/errorlens/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## TODO

- [ ] Show also warnings, weak warnings and typos and make the shown severity level configurable.
- [ ] The font foreground color should be configurable or at least should be loaded from the color scheme.
- [ ] Option to show the error description only in the line where the caret is.
- [ ] Given a pattern or some file extensions, never apply this plugin to those files.
- [ ] Intelligent parsing for error descriptions. In example: for kotlin not show the error code in between brackets
  and show only the description

## Contributing

* Fork the project and pull request when the job is done.
* Use the [Conventional Commits](https://www.conventionalcommits.org) commit standard.
* Provide test when possible.

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
