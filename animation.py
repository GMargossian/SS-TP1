import matplotlib.pyplot as plt
import json 

with open("data.json",'r') as file:
    data = json.load(file)
    particles: list = data["particles"]
    particle_ids: list = list(map(lambda p: p['id'], particles))
    print(particles)
    user_inp: str = input(f"Choose particle from: {particle_ids}\n")
    try:
        chosen_id: int = int(user_inp)
    except ValueError:
        print(f"invalid input, input must be a number from: {particle_ids}")
        exit(-1)
    if(chosen_id not in particle_ids):
        print(f"invalid input, input must be a number from: {particle_ids}")
        exit(-1)
    chosen = list(filter(lambda p: p['id'] == chosen_id,particles))[0]
    neighbours = list(filter(lambda p: p['id'] in chosen['neighbours'],particles))
    x_values = list(map(lambda p: p['posX'], particles))
    y_values = list(map(lambda p: p['posY'], particles))
    plt.scatter(x=x_values,y=y_values)
    plt.grid()
    plt.show()