import { Component, OnInit, ViewChild } from '@angular/core';
import { SaveMovieDTO, Movie, ShowMovieDTO } from '../../../model/movie.model';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { MovieService } from '../../../services/movie/movie.service';

@Component({
  selector: 'app-movie',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './movie.component.html'
})
export class MovieComponent implements OnInit {

  @ViewChild('movieForm') movieForm: NgForm = {} as NgForm;

  previewImage = '';

  movieList: Movie[] = [];
  currentMovie: ShowMovieDTO = {
    title: '',
    synopsis: '',
    year: 0,
    duration: 0,
    posterUrl: ''
  };
  tempMovie: ShowMovieDTO = {
    title: '',
    synopsis: '',
    year: 0,
    duration: 0,
    posterUrl: ''
  };
  charCount: number = 0;

  isEdit = false;
  isPosterChanged = false;

  constructor(private movieService: MovieService) { }

  ngOnInit(): void {
    this.getMovieList();
  }


  getMovieList() {
    // TODO: CHANGE TO API CALL LATER
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

  showMovieModal(isEdit: boolean, movie?: Movie) {
    this.previewImage = '';
    this.isEdit = isEdit;
    this.currentMovie = {
      title: movie?.title || '',
      synopsis: movie?.synopsis || '',
      year: movie?.year || 2024,
      duration: movie?.duration || 0,
      posterUrl: movie?.posterUrl || ''
    };

    if (isEdit) {
      this.tempMovie = {
        title: movie?.title || '',
        synopsis: movie?.synopsis || '',
        year: movie?.year || 2024,
        duration: movie?.duration || 0,
        posterUrl: movie?.posterUrl || ''
      };
    }

    this.charCount = isEdit ? this.currentMovie.synopsis.length : 0;

    const modal = document.getElementById('movie-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeMovieModal() {
    this.movieForm.resetForm();
    const modal = document.getElementById('movie-modal') as HTMLDialogElement;
    this.charCount = 0;
    this.isPosterChanged = false;
    this.previewImage = '';
    this.tempMovie = {} as ShowMovieDTO;
    modal.close();
  }

  isSaveButtonDisabled() {
    if (this.isEdit) {
      return this.currentMovie.title === this.tempMovie.title &&
        this.currentMovie.synopsis === this.tempMovie.synopsis &&
        this.currentMovie.year === this.tempMovie.year &&
        !this.isPosterChanged;
    } else {
      return !this.currentMovie?.title ||
        !this.currentMovie ||
        !this.currentMovie?.synopsis ||
        !this.currentMovie?.year ||
        this.previewImage === '';
    }
  }

  limitCharacters(event: any) {
    let value = event.target.value;

    if (value.length > 1000) {
      value = value.slice(0, 1000);
    }

    this.currentMovie.synopsis = value;
    this.charCount = this.currentMovie.synopsis.length;

    event.target.value = this.currentMovie.synopsis;
  }

  handlePaste(event: ClipboardEvent) {
    const pasteData = event.clipboardData?.getData('text') || '';
    const currentText = this.currentMovie.synopsis;

    let combinedText = currentText + pasteData;

    if (combinedText.length > 1000) {
      combinedText = combinedText.slice(0, 1000);
      event.preventDefault();
    }

    this.currentMovie.synopsis = combinedText;
    this.charCount = this.currentMovie.synopsis.length;

    (event.target as HTMLTextAreaElement).value = this.currentMovie.synopsis;
  }

  triggerFileInput() {
    const fileInput = document.getElementById('posterInput') as HTMLInputElement;
    fileInput.click();
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.isPosterChanged = true;
        this.previewImage = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }
}
