// Import Chart.js and React-Chart.js components
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';

// --- REMOVED ---
// import { generateRandomColor } from '../helpers';

// Register the components we need from Chart.js
ChartJS.register(ArcElement, Tooltip, Legend);

// --- NEW ---
// This is a new color generator *just for this chart*.
// It creates RGBA colors, which Chart.js loves.
const generateChartColors = (numColors) => {
  const colors = [];
  for (let i = 0; i < numColors; i++) {
    const r = Math.floor(Math.random() * 200); // 0-200 to avoid colors that are too light
    const g = Math.floor(Math.random() * 200);
    const b = Math.floor(Math.random() * 200);
    colors.push(`rgba(${r}, ${g}, ${b}, 0.7)`); // Add 0.7 opacity
  }
  return colors;
};

const CategoryChart = ({ summaryData }) => {

  // --- NEW --- Generate colors once
  const backgroundColors = generateChartColors(summaryData.length);
  const hoverColors = backgroundColors.map(color => color.replace('0.7', '1.0')); // Make opaque on hover

  // 1. Transform your data for Chart.js
  const chartData = {
    labels: summaryData.map(item => item.category),
    datasets: [
      {
        label: 'Spending',
        data: summaryData.map(item => item.total),
        // --- UPDATED ---
        backgroundColor: backgroundColors,
        hoverBackgroundColor: hoverColors, // --- NEW --- (nice hover effect)
        borderColor: '#363636', // --- UPDATED --- (dark border)
        borderWidth: 2,
      },
    ],
  };

  // 2. Set options for the chart (e.g., legend position)
  const chartOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          color: '#e2e8f0', // Set label color for dark mode
          font: {
            size: 14
          }
        }
      },
    },
  };

  // 3. Render the Doughnut chart component
  return (
    <div className="chart-container" style={{ position: 'relative', width: '100%', maxWidth: '400px', margin: '0 auto' }}>
      <Doughnut data={chartData} options={chartOptions} />
    </div>
  );
};

export default CategoryChart;

