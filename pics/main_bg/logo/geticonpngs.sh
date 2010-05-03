#!/bin/bash

rootfolder="./"
sizes="16 32 48 150 180 220 256"
files="kora.svg"

for size in $sizes; do
    #mkdir -p "${size}"
    for file in $files; do
        pngName=$( echo $file | cut -d . -f -1 )_${size}".png"
        inkscape --without-gui --export-png="${pngName}" --export-dpi=72 --export-background-opacity=0 --export-width=$size --export-height=$size ${rootfolder}/${file} > /dev/null
    done
done

