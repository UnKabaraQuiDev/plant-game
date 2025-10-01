import numpy as np
import matplotlib.pyplot as plt

# Generate example data from a Gaussian distribution
mean = 0
std_dev = 1
num_points = 100000
gaussian_data = np.random.normal(mean, std_dev, num_points)

# Apply natural logarithm to the data
linearized_data = np.log(gaussian_data)

# Plot original Gaussian data and linearized data
plt.figure(figsize=(10, 5))
plt.subplot(1, 2, 1)
plt.hist(gaussian_data, bins=20, color='blue', alpha=0.7)
plt.title('Original Gaussian Distribution')
plt.xlabel('Value')
plt.ylabel('Frequency')

plt.subplot(1, 2, 2)
plt.scatter(gaussian_data, linearized_data, color='red', alpha=0.7)
plt.title('Linearized Gaussian Distribution')
plt.xlabel('Original Value')
plt.ylabel('ln(Value)')

plt.tight_layout()
plt.show()
