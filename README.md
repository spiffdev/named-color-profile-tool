# named-color-profile-tool

A little tool for generating ICC named color profiles for use with Apache FOP and Apache Batik.

This is a **code dump** from some work on named colors back in 2009. This will most likely not be actively developed further but hopefully is useful for people needing to produce named colors (like PANTONE colors) in PDF using Apache FOP and/or Apache Batik.

This consists of two parts:

* Low-level Java code for writing a ICC named color profile.
* The start of a very simple Java desktop GUI to generate a profile (this is buggy!)

My recommendation is to just use the low-level writing code. But even that might not exactly produce files that are conformant to the ICC specification. It does produce files that are compatible with the counterpart reading code in Apache Batik and FOP.

There's a little patch (`apache-xml-graphics-commons-whitepoint-patch.diff`) here that needs to by apply against an old Apache XML Graphics Commons (~1.5) that adds a whitepoint parameter to the NamedColorProfile class. Therefore, xmlgraphics-commons.jar is not included here.
