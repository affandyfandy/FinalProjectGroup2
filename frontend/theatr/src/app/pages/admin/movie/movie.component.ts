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

  searchText = '';

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

  constructor(private movieService: MovieService) { }

  ngOnInit(): void {
    this.getMovieList();
  }


  getMovieList(page: number = 0) {
    this.movieService.getAllMovies(page, this.searchText).subscribe({
      next: (res: any) => {
        this.movieList = res.content;
        this.currentPage = page;
        this.totalPages = res.totalPages;
      },
      error: (err) => {
        this.showAlert('Failed to get movie list: ' + err.error.message, false);
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
        this.showAlert('Movie updated successfully', true);
      },
      error: (err) => {
        this.closeMovieModal();
        this.showAlert('Failed to create a movie: ' + err.error.message, false);
      }
    });
  }

  private createMovie(url: string) {
    const body: SaveMovieDTO = {
      title: this.currentMovie.title,
      synopsis: this.currentMovie.synopsis,
      year: this.currentMovie.year,
      posterUrl: url
    };
    this.movieService.createMovie(body).subscribe({
      next: (res: any) => {
        this.closeMovieModal();
        this.getMovieList();
        this.showAlert('Movie created successfully', true);
      },
      error: (err) => {
        this.closeMovieModal();
        this.showAlert('Failed to create a movie: ' + err.error.message, false);
      }
    });
  }

  private uploadPoster() {
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
          this.showAlert('Failed to upload poster: ' + err.error.message, false);
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

  showAlert(message: string, success: boolean) {
    this.isAlertSuccess = success;
    this.alertMessage = message;
    this.isShowAlert = true;
    setTimeout(() => {
      this.isShowAlert = false;
    }, 3000);
  }
}
