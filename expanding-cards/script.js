const panels= document.querySelectorAll('.panel');

panels.forEach(function(panel){
    panel.addEventListener('click', ()=>{
        removeActiveClasses();
        panel.classList.add('active');
        // console.log(panel.classList);
    });
});

function removeActiveClasses(){
    panels.forEach(panel =>{
        panel.classList.remove('active');
    });
}




