export const barChartData = (totalJobsByStatus) => {
  console.log(totalJobsByStatus)
  if(totalJobsByStatus === undefined){
    return;
  }

  return [
    {
      name: "Number of Jobs",
      data: totalJobsByStatus.map(item => item.numberOfJobs),
    },
  ];
}

export const barChartOptions = (totalJobsByStatus) => {
  console.log(totalJobsByStatus)
  if(totalJobsByStatus === undefined){
    return;
  }

  return {
    chart: {
      toolbar: {
        show: false,
      },
    },
    tooltip: {
      theme: "dark",
    },
    xaxis: {
      categories: totalJobsByStatus.map(item => item.status),
      labels: {
        style: {
          colors: "#A0AEC0",
          fontSize: "12px",
        },
      },
      show: true,
      axisBorder: {
        show: false,
      },
      
      
    },
    yaxis: {
      show: true,
      color: "#A0AEC0",
      labels: {
        show: true,
        style: {
          colors: "#A0AEC0",
          fontSize: "14px",
        },
        formatter: function (val) {
          return Math.round(val);
        }
      },
      forceNiceScale: true
    },
    fill: {
      colors: "#ED8936",
    },
    dataLabels: {
      enabled: false,
    },
    grid: {
      strokeDashArray: 5,
    },
    plotOptions: {
      bar: {
        borderRadius: 15,
        columnWidth: "15px",
      },
    },
    responsive: [
      {
        breakpoint: 768,
        options: {
          plotOptions: {
            bar: {
              borderRadius: 0,
            },
          },
        },
      },
    ],
  }
}

export function lineChartData (totalJobByDay) {
  console.log(totalJobByDay)
  if(totalJobByDay === undefined){
    return;
  }
  
  return [
    {
      name: "Applied",
      data: totalJobByDay.map(item => item.numberOfAppliedJobsByDay),
    }
  ];
}

export function lineChartOptions (totalJobByDay) {
  console.log(totalJobByDay)
  if(totalJobByDay === undefined){
    return;
  }

  const sortedDays = totalJobByDay.map(item => new Date(item.date).toISOString().substring(0, 10))
  return {
    chart: {
    toolbar: {
      show: false,
    },
  },
  tooltip: {
    theme: "dark",
  },
  dataLabels: {
    enabled: false,
  },
  stroke: {
    curve: "smooth",
  },
  xaxis: {
    type: "datetime",
    categories: sortedDays.sort((a, b) => new Date(a) - new Date(b)),
    axisTicks: {
      show: false
    },
    axisBorder: {
      show: false,
    },
    labels: {
      style: {
        colors: "#fff",
        fontSize: "12px",
      },
    },
  },
  yaxis: {
    labels: {
      style: {
        colors: "#fff",
        fontSize: "12px",
      },
    },
  },
  legend: {
    show: false,
  },
  grid: {
    strokeDashArray: 5,
  },
  fill: {
    type: "gradient",
    gradient: {
      shade: "light",
      type: "vertical",
      shadeIntensity: 0.5,
      inverseColors: true,
      opacityFrom: 0.8,
      opacityTo: 0,
      stops: [],
    },
    colors: ["#fff", "#3182CE"],
  },
  colors: ["#fff", "#3182CE"],
};
}