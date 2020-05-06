import * as React from 'react';
import { useStore } from 'effector-react';
import styled from 'styled-components';
import { Avatar } from 'antd';

import { $user } from 'features/user';

type UserProps = {
  className?: string;
}

export const User: React.FC<UserProps> = ({ className }) => {
  const user = useStore($user);

  if (!user) return null

  return (
    <UserBlock className={className}>
      {user.userName} <Avatar src={user.avatarUrl || ''} />
    </UserBlock>
  );
};

const UserBlock = styled.div`
  padding: 0 20px;
  display: flex;
  color: hsla(0,0%,100%,.65) !important;
  font-size: 14px;
`;
