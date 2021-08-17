import matplotlib.pyplot as plt
import json 
import os
import sys
print(sys.argv)

if len(sys.argv) > 1:
    BENCHMARK_RESULTS_PATH: str = sys.argv[1]
else:
    BENCHMARK_RESULTS_PATH: str = "cim/results/benchmark_results.json"

if os.path.exists(BENCHMARK_RESULTS_PATH) and os.access(BENCHMARK_RESULTS_PATH, os.R_OK):
    with open(BENCHMARK_RESULTS_PATH,'r') as file:
        data = json.load(file)
        best_m_results: list = data["best_m_results"]
        M_values = list(map(lambda arr: arr[0], best_m_results))
        M_value_time = list(map(lambda arr: arr[1], best_m_results))


        comparison_results: list = data["comparison_results"]
        N_values = list(map(lambda arr: arr[0], comparison_results))
        CIM_N_time = list(map(lambda arr: arr[1], comparison_results))
        BF_N_time = list(map(lambda arr: arr[2], comparison_results))

        plt.plot(M_values,M_value_time)
        plt.grid()
        plt.ylabel('ms')
        plt.xlabel('M values')
        plt.show()

        
        plt.plot(N_values,CIM_N_time, "r",label="CIM")
        plt.plot(N_values, BF_N_time, "b", label="BF")
        plt.grid()
        plt.ylabel('ms')
        plt.xlabel('N values')
        plt.legend(loc='upper left')
        plt.show()
        
else:
    print("Benchmark file not found, exiting...")