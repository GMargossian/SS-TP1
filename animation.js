var particle_list = [];
var RC;
var M;
var hasWalls;

var directions = [[1,0],[0,1], [1,1], [-1,0], [0,-1], [-1,-1]];

class Particle{
    constructor(id,x,y,radius,neighbours,color){
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.neighbours = neighbours;
        this.color = color;
    }

    draw(context, L){
        context.beginPath();
        context.arc( this.x, this.y, this.radius, 0, Math.PI * 2, false);
           
        // Fill Stroke
        context.fillStyle = this.color;
        context.fill();
        context.stroke();
        context.font = "15px Arial";
        context.fillStyle = "white"
        context.textAlign = "center";
        context.textBaseline = "middle";
        context.fillText(this.id,this.x,this.y);
        context.closePath();

        if(!this.hasWalls){

            
            for (const direc of directions) {
                // console.log(direc);

                context.beginPath();
                context.arc( this.x + direc[0] * L, this.y + direc[1] * L, this.radius, 0, Math.PI * 2, false);
                context.fillStyle = this.color;
                context.fill();
                context.stroke();
                context.font = "15px Arial";
                context.fillStyle = "white"
                context.textAlign = "center";
                context.textBaseline = "middle";
                context.fillText(this.id,this.x + direc[0] * L, this.y + direc[1] * L);
                context.closePath();
            }
        }
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
    // console.log(event.target.result);
    var data = JSON.parse(event.target.result);
    // console.log(data['particles'])
    
    scale = 50;
     
    canvas.width = data['L']*scale;
    canvas.height = data['L']*scale;
    RC = data['RC'] * scale;
    M = data['M'];
    hasWalls = data['hasWalls'];

    particle_list = [];
    alwaysDraw();
    data['particles'].forEach((particle) =>{
        // console.log(particle)
        let new_particle = new Particle(particle['id'],particle['posX'] * scale, particle['posY'] * scale, particle['radius'] * scale, particle['neighbours'], "green");
        // Generate x and y values between 0 to 140 considering 30 pt radius
        new_particle.draw(context, canvas.width);
        particle_list.push(new_particle);
    })
}

function resetParticleColor(){
    particle_list.forEach((p)=>{
        p.color = "green";
    })
}

function getClickedParticle(x,y){
    for(let particle of particle_list){
        // console.log(particle.id + ": " + particle.clickParticle(x,y));
        if(particle.clickParticle(x,y)){
            // console.log("returning: " + particle)
            return particle;
        }
    }
    return null;
}
function alwaysDraw(){
    // drawWindowBorders();
    drawGrid();
}

// function drawWindowBorders(){
//     context.beginPath();
//     context.moveTo(0, 0);
//     context.lineTo(canvas.width, 0);
//     context.moveTo(canvas.width, 0);
//     context.lineTo(canvas.width, canvas.height);    
//     context.moveTo(canvas.width, canvas.height);
//     context.lineTo(0, canvas.height);    
//     context.moveTo(0, canvas.height);
//     context.lineTo(0, 0);
//     context.stroke();
//     context.closePath();
// }

function drawGrid(){

    for(var i = 0 ; i <= canvas.width ; i+= Math.floor(canvas.width/this.M)){
        console.log(i);
        context.beginPath();
        context.moveTo(0,i);
        context.lineTo(canvas.height,i);
        context.stroke();
        context.closePath();
    }
    for(var i = 0 ; i <= canvas.height ; i+= Math.floor(canvas.height/this.M)){
        context.beginPath();
        context.moveTo(i,0);
        context.lineTo(i,canvas.width);
        context.stroke();
        context.closePath();
    }
    // context.stroke();
    // context.closePath();
}


canvas.addEventListener('click', (event) =>{
    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    // iterate particles in particles:
    if(particle_list.length !== 0){
        let clicked = getClickedParticle(x,y);
        // console.log(clicked);
        if(clicked !== null){
            // console.log("particle clicked: " + clicked.id);
            resetParticleColor();
            clicked.color = "red";

            const neighbours = particle_list.filter(p => clicked.neighbours.includes(p.id))
            neighbours.forEach(n=> n.color = "pink");
            context.clearRect(0, 0, canvas.width, canvas.height);
            
            context.beginPath();
            context.arc( clicked.x, clicked.y, clicked.radius + RC, 0, Math.PI * 2, false); 
            context.stroke();
            context.closePath();

            if(!this.hasWalls){
                for (const direc of directions) {
                    context.beginPath();
                    context.arc( clicked.x + direc[0] * canvas.width, clicked.y + direc[1] * canvas.width, clicked.radius + RC, 0, Math.PI * 2, false); 
                    context.stroke();
                    context.closePath();
                }
            }

            alwaysDraw();
            particle_list.forEach(p => p.draw(context, canvas.width));
        }
    }

})


