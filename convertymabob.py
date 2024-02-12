

song = open('assets/charts/Relative Fiction.sm',"r")

for line in song:
    for char in line:
        if hex(char) != "0x0D":
            continue
        line.remove(char)
    print(line)
            
    
