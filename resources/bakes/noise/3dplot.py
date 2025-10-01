import matplotlib.pyplot as plt
import numpy as np

a = [(0, 0.5), (0, 0.4), (0.8, 1)]
b = [(0.4, 1), (0.2, 0.8), (0.5, 1)]
c = [(0.6, 1), (0, 0.4), (0.3, 1)]

# prepare some coordinates
x, y, z = np.indices((10, 10, 10))/10

# draw cuboids in the top left and bottom right corners, and a link between
# them
cube1 = (x > a[0][0]) & (x < a[0][1]) & (y > a[1][0]) & (y < a[1][1]) & (z > a[2][0]) & (z < a[2][1])
cube2 = (x > b[0][0]) & (x < b[0][1]) & (y > b[1][0]) & (y < b[1][1]) & (z > b[2][0]) & (z < b[2][1])
cube0 = (x > c[0][0]) & (x < c[0][1]) & (y > c[1][0]) & (y < c[1][1]) & (z > c[2][0]) & (z < c[2][1])

# combine the objects into a single boolean array
voxelarray = cube1 | cube2 | cube0

# set the colors of each object
colors = np.empty(voxelarray.shape, dtype=object)
colors[cube0] = 'red'
colors[cube1] = 'blue'
colors[cube2] = 'green'

# and plot everything
ax = plt.figure().add_subplot(projection='3d')
ax.voxels(voxelarray, facecolors=colors, edgecolor='k')

plt.show()