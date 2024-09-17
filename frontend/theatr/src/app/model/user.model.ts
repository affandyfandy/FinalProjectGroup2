export interface User {
    id?: string;
    email?: string;
    balance?: number;
    role?: string;
    name?: string;
    createdTime?: Date;
    updatedTime?: Date;
}

export interface LoginDTO {
    email: string;
    password: string;
}

export interface RegisterDTO {
    email: string;
    password: string;
    name: string;
}