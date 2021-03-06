#!/bin/bash

rootfolder="svg/"
sizes="48 32 64"
files=`ls $rootfolder`

for size in $sizes; do
    mkdir -p "${size}"
    for file in $files; do
        pngName=$( echo $file | cut -d . -f -1 )".png"
        inkscape --without-gui --export-png="${size}/${pngName}" --export-dpi=72 --export-background-opacity=0 --export-width=$size --export-height=$size ${rootfolder}/${file} > /dev/null
    done
done

