import { Component, OnInit, ViewChild } from '@angular/core';
import { SaveMovieDTO, Movie, ShowMovieDTO } from '../../../model/movie.model';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { MovieService } from '../../../services/movie/movie.service';
import { MessageConstants } from '../../../config/app.constants';

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

  searchText = '';

  isLoading = false;

  movieList: Movie[] = [];
  currentMovie: ShowMovieDTO = {
    id: '',
    title: '',
    synopsis: '',
    year: 0,
    duration: 0,
    posterUrl: ''
  };
  tempMovie: ShowMovieDTO = {
    id: '',
    title: '',
    synopsis: '',
    year: 0,
    duration: 0,
    posterUrl: ''
  };
  charCount: number = 0;

  isEdit = false;
  isPosterChanged = false;

  currentPage = 1;
  totalPages = 1;

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  isListLoading = false;

  constructor(private movieService: MovieService) { }

  ngOnInit(): void {
    this.getMovieList();
  }


  getMovieList(page: number = 0) {
    this.isListLoading = true;
    this.movieService.getAllMovies(page, this.searchText, 5).subscribe({
      next: (res: any) => {
        this.movieList = res?.content ?? [];
        this.currentPage = page;
        this.totalPages = res.totalPages;
        this.isListLoading = false;
      },
      error: (err) => {
        this.showAlert(MessageConstants.GET_MOVIE_LIST_FAILED(err), false);
        this.isListLoading = false;
      },
      complete: () => {
        this.isListLoading = false;
      }
    });
  }

  onSaveClick() {
    if (this.isEdit && this.isPosterChanged) {
      this.uploadPoster();
    } else if (!this.isEdit) {
      this.uploadPoster();
    } else {
      this.updateMovie();
    }
  }

  private updateMovie(url?: string) {
    this.isLoading = true;
    const body: SaveMovieDTO = {
      title: this.currentMovie.title,
      synopsis: this.currentMovie.synopsis,
      year: this.currentMovie.year,
      posterUrl: url ?? this.currentMovie.posterUrl
    };
    this.movieService.updateMovie(this.currentMovie.id, body).subscribe({
      next: (res: any) => {
        this.closeMovieModal();
        this.getMovieList(this.currentPage);
        this.isLoading = false;
        this.showAlert(MessageConstants.UPDATE_MOVIE_SUCCESS, true);
      },
      error: (err) => {
        this.isLoading = false;
        this.closeMovieModal();
        this.showAlert(MessageConstants.UPDATE_MOVIE_FAILED(err), false);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  private createMovie(url: string) {
    this.isLoading = true;
    const body: SaveMovieDTO = {
      title: this.currentMovie.title,
      synopsis: this.currentMovie.synopsis,
      year: this.currentMovie.year,
      posterUrl: url
    };
    this.movieService.createMovie(body).subscribe({
      next: (res: any) => {
        this.isLoading = false;
        this.closeMovieModal();
        this.getMovieList();
        this.showAlert(MessageConstants.CREATE_MOVIE_SUCCESS, true);
      },
      error: (err) => {
        this.isLoading = false;
        this.closeMovieModal();
        this.showAlert(MessageConstants.CREATE_MOVIE_FAILED(err), false);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  private uploadPoster() {
    this.isLoading = true;
    const file = (document.getElementById('posterInput') as HTMLInputElement).files?.[0];
    if (file) {
      this.movieService.uploadImagePoster(file).subscribe({
        next: (res: any) => {
          if (this.isEdit) {
            this.updateMovie(res.posterUrl);
          } else {
            this.createMovie(res.posterUrl);
          }
        },
        error: (err) => {
          this.isLoading = false;
          this.showAlert(MessageConstants.UPLOAD_POSTER_FAILED(err), false);
        }
      });
    }
  }

  showMovieModal(isEdit: boolean, movie?: Movie) {
    this.previewImage = '';
    this.isEdit = isEdit;
    this.currentMovie = {
      id: movie?.id || '',
      title: movie?.title || '',
      synopsis: movie?.synopsis || '',
      year: movie?.year || 2024,
      duration: movie?.duration || 0,
      posterUrl: movie?.posterUrl || ''
    };

    if (isEdit) {
      this.tempMovie = {
        id: movie?.id || '',
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
    if (this.isLoading) {
      return true;
    } else {
      if (this.isEdit) {
        return (this.currentMovie.title === this.tempMovie.title &&
          this.currentMovie.synopsis === this.tempMovie.synopsis &&
          this.currentMovie.year === this.tempMovie.year &&
          !this.isPosterChanged) ||
          !this.currentMovie?.title ||
          !this.currentMovie ||
          !this.currentMovie?.synopsis ||
          !this.currentMovie?.year;
      } else {
        return !this.currentMovie?.title ||
          !this.currentMovie ||
          !this.currentMovie?.synopsis ||
          !this.currentMovie?.year ||
          this.previewImage === '';
      }
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

  showAlert(message: string, success: boolean) {
    this.isAlertSuccess = success;
    this.alertMessage = message;
    this.isShowAlert = true;
    setTimeout(() => {
      this.isShowAlert = false;
    }, 3000);
  }
}
