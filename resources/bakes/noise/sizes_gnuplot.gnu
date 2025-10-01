set terminal png
set output 'distribution_plot.png'

set boxwidth 0.5
set style fill solid
plot 'sizes.txt' using 1:($0) smooth frequency with boxes
