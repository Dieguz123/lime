import {Provider} from './provider';
import {User} from './user';

export interface Room {
  inviteCode: string;
  creator: User;
  partner: User | null;
  providers: Provider[];
}
