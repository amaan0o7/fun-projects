import React, { useState } from 'react';
import people from './data';
import { FaChevronLeft, FaChevronRight, FaQuoteRight } from 'react-icons/fa';

const Review = () => {
  const[index,setIndex]=useState(0);
  const {name,job,image,text}=people[index];
  const checkSize=(number)=>{
    if(number>people.length-1)
    {
      return 0;
    }
    if(number<0){
      return (people.length-1);
    }
    return number;
  }
  return (<article className="review">
  <div className="img-container">
    <img src={image} alt={name} className='person-img'></img>
    <span className='quote-icon'><FaQuoteRight/>
    </span>
  </div>
  <h4 className="author">{name}</h4>
  <p className='job'>{job}</p>
  <p className='info'>{text}</p>
  <div className="button-container">
    <button className="prev-btn" onClick={()=>setIndex(checkSize(index-1))}>
      <FaChevronLeft/>
    </button>
    <button className="next-btn" onClick={()=>setIndex(checkSize(index+1))}>
      <FaChevronRight/>
    </button>
  </div>
  <button className="random-btn" onClick={()=>setIndex(Math.floor(Math.random()*people.length))}>
      surprise me
    </button>
  </article>);
};

export default Review;
