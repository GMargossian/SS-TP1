var particle_list = [];


class Particle{
    constructor(id,x,y,radius,neighbours,color){
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.neighbours = neighbours;
        this.color = color;
    }

    draw(context){
        context.beginPath();
        context.arc( this.x, this.y, this.radius, 0, Math.PI * 2, false);
        
        // Fill Stroke
        context.fillStyle = this.color;
        context.fill();
        context.stroke();
        context.font = "15px Arial";
        context.fillStyle = "blue"
        context.textAlign = "center";
        context.textBaseline = "middle";
        context.fillText(this.id,this.x,this.y);

        context.closePath();
    }

    clickParticle(xMouse,yMouse){
        const distance = Math.sqrt(( (xMouse - this.x ) * (xMouse - this.x)) + ( (yMouse - this.y) * ( yMouse - this.y)));
        
        if (distance < this.radius){
            return true;
        } else {
            return false;
        }
        
    }

    
}
Particle.prototype.toString = function particleToString() {
    let to_return = "Particle{ id: " + this.id + ", x: " + this.x + ", y: " + this.y + ", radius: " + this.radius + ", color: " + this.color + ", neighbours: " + this.neighbours + " }";
    return to_return;
}

var canvas = document.getElementById("canvas");
var context = canvas.getContext("2d");
const dataSelector = document.getElementById('data-selector');


dataSelector.addEventListener('change', (event) => {
    var reader = new FileReader();
    reader.onload = onReaderLoad;
    reader.readAsText(event.target.files[0]);
  }
);

function onReaderLoad(event){
    console.log(event.target.result);
    var data = JSON.parse(event.target.result);
    console.log(data['particles'])
    scale = 70;
    canvas.width = data['L'] * scale;
    canvas.height = data['L'] * scale;
    particle_list = [];
    data['particles'].forEach((particle) =>{
        console.log(particle)
        let new_particle = new Particle(particle['id'],particle['posX'] * scale, particle['posY'] * scale, particle['radius'] * scale, particle['neighbours'], "green");
        // Generate x and y values between 0 to 140 considering 30 pt radius
        new_particle.draw(context);
        particle_list.push(new_particle);
    })
    drawWindowBorders();
}

function resetParticleColor(){
    particle_list.forEach((p)=>{
        p.color = "green";
    })
}

function getClickedParticle(x,y){
    for(let particle of particle_list){
        console.log(particle.id + ": " + particle.clickParticle(x,y));
        if(particle.clickParticle(x,y)){
            console.log("returning: " + particle)
            return particle;
        }
    }
    return null;
}

function drawWindowBorders(){
    context.beginPath();
    context.moveTo(0, 0);
    context.lineTo(canvas.width, 0);
    context.moveTo(canvas.width, 0);
    context.lineTo(canvas.width, canvas.height);    
    context.moveTo(canvas.width, canvas.height);
    context.lineTo(0, canvas.height);    
    context.moveTo(0, canvas.height);
    context.lineTo(0, 0);
    context.stroke();
    context.closePath();
}


canvas.addEventListener('click', (event) =>{
    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    // iterate particles in particles:
    if(particle_list.length !== 0){
        let clicked = getClickedParticle(x,y);
        console.log(clicked);
        if(clicked !== null){
            console.log("particle clicked: " + clicked.id);
            resetParticleColor();
            clicked.color = "red";



            const neighbours = particle_list.filter(p => clicked.neighbours.includes(p.id))
            neighbours.forEach(n=> n.color = "pink");
            context.clearRect(0, 0, canvas.width, canvas.height);
            context.beginPath();
            context.arc( clicked.x, clicked.y, clicked.radius + 200, 0, Math.PI * 2, false); //TODO: Cambiar al RC
            context.stroke();
            context.closePath();
            drawWindowBorders();
            particle_list.forEach(p => p.draw(context));
        }
    }

})


