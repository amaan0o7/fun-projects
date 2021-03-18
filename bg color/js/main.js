// color array
let colors = ['blue', 'yellow', 'black', 'red', 'brown', 'orange'];
// get button
let button=document.getElementById('button');
// add event listener
button.addEventListener('click',function(){
    // randomizer
    var randomColor=colors[Math.floor(Math.random()*colors.length)];
    // get container
    document.getElementById('container').style.background=randomColor;
})