import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import style from "./Login.module.css"
import jwt_decode from "jwt-decode";

import RefreshToken from "../../Components/RefreshToken";

// 리덕스 저장
import { useDispatch } from 'react-redux';
import { setUser } from '../../redux/reducers/userSlice';


export default function Login() {

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };


  // 로그인
  // 로그인 성공 후 토큰과 유저 pk값을 로컬에 저장
  const handleLogin = () => {
    console.log(`이메일: ${formData.email} 비밀번호: ${formData.password}`)

    console.log(process.env.REACT_APP_URL)
    axios.post(`${process.env.REACT_APP_URL}/member/login`, {
      email: formData.email,
      password: formData.password
    })
      .then((res) => {
        console.log(res)
        console.log(res.headers)
        // 로컬 스토리지에 데이터 저장
        localStorage.setItem('memberNickName', res.data.memberNickName);
        localStorage.setItem('totalPoint', res.data.totalPoint);
        localStorage.setItem('totalScore', res.data.totalScore);
        localStorage.setItem('totalStudyTime', res.data.totalStudyTime);
        
        // decodedToken, accessToken 로컬에 저장 - 유저 정보(memberId,memberName,sub,exp,iat)
        const authorizationHeader = res.headers.authorization;
        const decodedToken = jwt_decode(authorizationHeader);
        const accessToken = authorizationHeader.substring(7);
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('decodedToken', JSON.stringify(decodedToken));

        // 로컬에서 decodedToken꺼내기
        const tokenInfo = localStorage.getItem('decodedToken');
        console.log(JSON.parse(tokenInfo));
        const parseJwt = JSON.parse(tokenInfo);
        console.log(parseJwt.sub);
        localStorage.setItem('refreshtoken',res.headers.refreshtoken);

        RefreshToken()
        dispatch(setUser(res.data));
        navigate("/MyPage");
      })
      .catch((err) => {
        console.log(err)
      });
  }



  return (
    <div className={style.Login_box}>
      <img className={style.login_img} src="./Assets/Login_img01.jpg" alt="" />

      <div className={style.login}>
            <h1 className={style.login_title}>로그인</h1>
            <p>
              <input className={style.loginInput}
                type="text"
                placeholder="이메일"
                name="email"
                value={formData.email}
                onChange={handleChange} />
              <br />
              <input className={style.loginInput}
                type="password"
                placeholder="비밀번호"
                name="password"
                value={formData.passWord}
                onChange={handleChange} />
            </p>
            <div className={style.Login_box_find}>
              <h5 className={style.password_find}>
                비밀번호 찾기</h5>
              <h5 className={style.signup}>
                회원가입</h5>
            </div>
            <button className={style.loginBtn}
              onClick={handleLogin}>
              로그인</button>

        <div className={style.line1}></div>
        <p>간편로그인</p>
        <div className={style.line2}></div>

        <img className={style.loginGoogleLogo}
          src="https://cdn-icons-png.flaticon.com/512/2991/2991148.png" alt="" />
        {/* kakao 로그인 */}
        <img className={style.loginKakaoLogo}
          src="https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/KakaoTalk_logo.svg/800px-KakaoTalk_logo.svg.png"
          alt="" />

        <img className={style.loginNaverLogo}
          src="/Assets/네이버.png" alt="" />

      </div>
    </div>
  )
}