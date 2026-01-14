export interface User {
  id: number;
  username: string;
  email: string;
  role: string;
  enabled: boolean;
}

export interface CreateUser {
  username: string;
  email: string;
  password: string;
  role: string;
}

export interface UpdateUser {
  username: string;
  email: string;
  password?: string;
  role: string;
}