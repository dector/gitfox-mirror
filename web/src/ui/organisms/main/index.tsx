import * as React from 'react';
import styled from 'styled-components';
import { Layout } from 'antd';

type MainProps = {
  children: React.ReactNode;
};

const { Content } = Layout;

export const Main: React.FC<MainProps> = ({ children }) => (
  <MainContainer>{children}</MainContainer>
);

const MainContainer = styled(Content)`
  background-color: #fff;
  padding: 12px 48px;
  min-height: calc(100vh - 128px);
`;
