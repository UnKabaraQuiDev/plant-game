data1 = dlmread('plants_dist1.txt');
data2 = dlmread('plants_dist2.txt');

% Combine data from both files
combined_data = [data1; data2];

% Determine the range and number of bins
min_value = 0;
max_value = 3;
num_bins = 100; % You can adjust the number of bins as needed

% Plot both datasets on the same graph with the same range and bins
hold on;
hist(data1, min_value:(max_value-min_value)/num_bins:max_value);
hist(data2, min_value:(max_value-min_value)/num_bins:max_value);
hold off;

xlim([min_value, max_value]);

% Add legend
legend('JNI', 'Raw Java');

% Save plot as PNG file
print -dpng plants_dist_comparison_plot1.png