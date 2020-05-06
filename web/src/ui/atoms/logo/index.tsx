import * as React from 'react'
import styled from 'styled-components'
import { Link } from 'react-router-dom'

type LogoProps = {
  to: string;
  icon?: React.ReactNode;
}

export const Logo: React.FC<LogoProps> = ({ to, icon }) => {
  return (
    <LogoContanter to={to}>
      {icon} <span className="logo-text">GitFox</span>
    </LogoContanter>
  )
}

const LogoContanter = styled(Link)`
  font-weight: 900;
  color: #fff;

  .anticon-gitlab {
    bottom: -4px;
    position: relative;
  }

  .logo-text {
    position: relative;
    bottom: -1px;
    margin-left: 12px;
  }
`;
