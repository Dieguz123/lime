import {User} from './user';

export interface Room {
  id: number;
  inviteCode: string;
  creator: User;
  partner: User | null;
}
