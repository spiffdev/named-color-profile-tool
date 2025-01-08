# named-color-profile-tool

A little tool for generating ICC named color profiles for use with Apache FOP and Apache Batik.

This is a **code dump** from some work on named colors back in 2009. This will most likely not be actively developed further but hopefully is useful for people needing to produce named colors (like PANTONE colors) in PDF using Apache FOP and/or Apache Batik.

This consists of two parts:

* Low-level Java code for writing a ICC named color profile.
* The start of a very simple Java desktop GUI to generate a profile (this is buggy!)

Jeremias' recommendation is to just use the low-level writing code. But even that might not exactly produce files that are conformant to the ICC specification. It does produce files that are compatible with the counterpart reading code in Apache Batik and FOP.

Contains a patched version of apache xmlgraphics-commons-1.5. That lib was built from source, after applying the patch `apache-xml-graphics-commons-whitepoint-patch.diff` that adds a whitepoint parameter to the NamedColorProfile class.

There appears to currently be no functionality to read ICC files, just write them.

## To run

Click the play button that Intellij Idea puts next to the signature of NCPApplication.