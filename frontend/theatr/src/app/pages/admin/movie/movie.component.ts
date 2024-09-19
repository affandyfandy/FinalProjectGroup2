import { Component, OnInit } from '@angular/core';
import { Movie } from '../../../model/movie.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-movie',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './movie.component.html'
})
export class MovieComponent implements OnInit {
  movieList: Movie[] = [];
  currentMovie?: Movie;

  constructor() { }

  ngOnInit(): void {
    this.getMovieList();
  }


  getMovieList() {
    for (let i = 0; i < 10; i++) {
      const movie: Movie = {
        id: '1',
        title: 'Inside Out',
        posterUrl: 'https://image.tmdb.org/t/p/w1280/vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg',
        synopsis: "Teenager Riley's mind headquarters is undergoing a sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t sure how to feel when Anxiety shows up. And it looks like she’s not alone. ",
        duration: 15,
        year: 2024
      };
      this.movieList.push(movie);
    }
  }
}
