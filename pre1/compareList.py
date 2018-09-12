import matplotlib.pyplot as plt
import numpy as np
from scipy.interpolate import spline

arrayList = []
blockList = []
linkedList = []
with open('out.txt', 'rb') as f:
    i = 0
    for line in f.readlines():
        tmp = float(line[line.find(':') + 1:-3])
        if tmp > 0.02: tmp = 0.02
        if i is 0: arrayList.append(tmp)
        elif i is 1: blockList.append(tmp)
        else: linkedList.append(tmp)
        i = (i + 1) % 3
y = [i for i in range(len(arrayList))]

# ny = np.array(y)
y = np.array(y)
ny = np.linspace(y.min(), y.max(), 300)
arrayList = spline(y, np.array(arrayList), ny)
blockList = spline(y, np.array(blockList), ny)
linkedList = spline(y, np.array(linkedList), ny)
plt.plot(ny, arrayList, 'b')
plt.plot(ny, blockList, 'g')
plt.plot(ny, linkedList, 'r')
plt.title("Compare List")
plt.xlabel("Data Scale(*100 + 10000)")
plt.ylabel("Average time(ms)")
plt.show()