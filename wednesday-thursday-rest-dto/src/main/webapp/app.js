const rootDiv = document.querySelector("#root");
const input = document.createElement("input");
const userBtn = document.createElement("button");
const getAllBtn = document.createElement("button");

userBtn.innerText = "Get Movie";
getAllBtn.innerText = "Get all Movies";

document.body.appendChild(input);
document.body.appendChild(userBtn);
document.body.appendChild(getAllBtn);

const getMovieByName = e => {
  e.preventDefault();
  rootDiv.innerHTML = "";
  const p = document.createElement("p");
  rootDiv.appendChild(p);
  fetch(`api/movie/name/${input.value}`)
    .then(response => {
      return response.json();
    })
    .then(json => {
      p.innerText = JSON.stringify(json, undefined, 2);
    });
};

const getMovies = e => {
  e.preventDefault();
  rootDiv.innerHTML = "";
  const p = document.createElement("p");
  rootDiv.appendChild(p);
  fetch(`api/movie/all`)
    .then(response => {
      return response.json();
    })
    .then(json => {
      p.innerText = JSON.stringify(json, undefined, 2);
    });
};

getAllBtn.addEventListener("click", getMovies);
userBtn.addEventListener("click", getMovieByName);
