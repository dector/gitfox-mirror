import * as React from 'react';
import styled from 'styled-components';

import { Button, Typography } from 'antd';

import { Form, Input } from 'ui/atoms';

import { submitForm, loginStore } from './model';

const fields = {
  privateToken: {
    store: loginStore,
    config: {
      placeholder: 'Provate Token',
      name: 'privateToken',
    },
    style: {
      textAlign: 'center',
    }
  },
}
export const LoginPage: React.FC = () => {
  return <Container>
    <Typography.Title level={4}>Authentication</Typography.Title>
    <Form handleSubmit={submitForm}>
      <Input {...fields.privateToken} />
      <Actions>
        <Button type="primary" htmlType="submit">
          Log in
      </Button>
      </Actions>
    </Form>
  </Container>
};

const Container = styled.div`
  margin: auto;
  background-color: #fff;
  padding: 24px 24px 24px 24px;
  border-radius: 3px;
  min-width: 300px;

  h4 {
    text-align: center;
    padding-bottom: 8px;
  }
`;

const Actions = styled.div`
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  button {
    width: 100%;
    height: 40px;
  }
`;
