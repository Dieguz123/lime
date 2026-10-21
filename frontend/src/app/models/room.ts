import {User} from './user';

export interface Room {
  inviteCode: string;
  creator: User;
  partner: User | null;
}
