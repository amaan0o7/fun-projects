let btn=document.getElementById('btn');
let output=document.getElementById('output');
btn.addEventListener('click',function(){
    let inputVal=document.getElementById("word").value;
    output.innerHTML=inputVal.length;
});