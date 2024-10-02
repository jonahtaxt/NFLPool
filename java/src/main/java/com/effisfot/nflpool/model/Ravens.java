package com.effisfot.nflpool.model;

public class Ravens extends Team {
    public Ravens () {
        this.setPoolName("BALTIMORE");
        this.setLogo("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAK20lEQVR42u1ZCVBUZxJmhmHue7gRBeRQgihBERQRSTSCJ4gGEs8oGlRAQPFKwhF04qoQUBBBIuGQrEQUVg6ReBAPEhWNa7kxmGiSSrFJZY21Vo51Xb7t/yFTpjRqFKO7vlfVNcO8N/O6v+7++uuHGQCzp9nMeAB4AHgAeAB4AHgAeAB4AHgAeAB4AHgAeAB4AHgAeAB4AJ5mADo6Osy2b9/e41Zd9a6spanc48yxiqDPTlWOJ5tCFkE29viB8iGNtaW2lRUlwkdx79+y9vb22wE4ePCg2cMeFiKB0MvFwj9qtGJV8kvq9zcl6b/IStD9Z+VMDWLDVZgVpkD06C6bEarAq/QZO7cxTvejMVbb+sp4Zba/lyRMKRdIzR7hwUDoMQAoaMFAV/HQBZOV+VuW6b9dPVuDSSPkGNBXDJVcCIFACAsLLaTSXpDLXaBQuEEhd4VM1gdisRWEQgl5YsZdO8hNjOkvKLB+se5qYpR6h4+7OOCJBUAuFYgp0PmU4U8pewj1l0GnomBFGmjUz8LGejycescgJNiI+THbkZ62B5ty96N42yEUFx9CdtY+LImvwDD/NXBxSoCDfTQM+hGQSR1hLhSiv5MFCASkztXsc3MUeT5RAAzuJx5OQR97a6EWIwZJIJUIKbOusLONgN/gVCxNfg+lJR/gcONu/PVoEVrrVqO5fAb+UhCK3blB2LM5GA3F4ThctRAnD25DXu5euLuuhFvfLuvdaw70umEEpgLDvSWsIv4xeoh0KgHRVyg0Ezw2ACw1Qkvq7dK8ZH3nGD8pOWgBndYfbq6LkZxYidqq9/FRQwaa3o3mgt1fNh0fVi/Bxw2pOH1gPc625OJwTQb270xBdU4gilcZOPugYjamRuaZAOg2V5dlsDSEQKOUIjlajZwl+nZqNeNjASAsQDYjf6m+Y+lLaujVQiiV/Shr8Vj3Vh3OfvI56nbtQt7GrTCu2YXEJTsw/eVCREZswu7qj3ChbQ+O7E7CVxfPwmfQDOp9GY4e2msC4FBVAkYMX8sF7eIUz1WSrc0kaqFXuc+cesdSa1hj2nNyjjRDfKWRErHA/A8BgG4kjp+m2lq43IBJQXKYm0s455hjQ/3ehK9P2m2ZY+YzMA0nWtu4SmBBHq6KxVvG+q4gXbxwvCkXZRnOaKxMxYJ5hbd9v5f9y0SeOkgktrC3mwoX50QiTkf0sjZH3FTV51SJWx85ABqFUJMRoz2Qt1TPkZJQYEG9GQCtxpec6U0O6k0Mb2M9zuS8h9sq1NZ8jJq8EFOWv/rsQ+Tn7kHZu8348vMzaGkowWsry+Ht9QaVegplfQo0mmc5EhTf/F2RSE1eCiCXOcPB7kU491lEU0WEMUNlXCUQ6YofGQAUvGzFDE194QoDnvUQc6OKmUBgBrVCCAcrczhSNsQiwc1zAiKvVzgAFi0sw/nWElPwtfmj0VT9J+wpex1HalOJBEfh0PtJ2Lp5J/q5r+JK3N42EtZWoVCrBtBo7Lpf5pCRqAgaB3+pjpsS7LeVCg/QBELkKDlmhiremTFWUUz6IWfKKPlyIuQxPQIAy/zqWZoG5vxIHym8XcV4aYwCq2ZpkL9MbwqMWU6iHu6OFpzDVobnODY/c/IMKo2epmvOHNtLJTyNKqc/AgMD0fhOJPd5XeF4JMSXc4Gx7FpZPs8BIJU6wEIgwKWFK+BgLoYdmUik6rqG7tGdjG6TigWYECjD63M0P5EO2Td+uGyBncHc7oEAYD2fuUB7kDm4OVnPBcjeF6Tov6Ub7I+ZqMwJHyl/bXGkqpA+v87OTQmWc46wDM6aWYSTTZmm4BnbZ2c13Rxv0YiKisLuTSO5c3u3hnEawcE+Cmr1QAJoCLRaP+69Qt4XgzW98LxMBymJKfb7jA9Y+3UH7u/vD19fX2odjak6gwZJuSQRYX/mZCey/V0A0Iw1I/FRfNP5G2lzNQco2CVMiNxp/lKV1LNrA7y61Fwfx/mo2XMCO9b2MwHwSUs+hgcYqTJWIHTsXLTU52JDggfmTBqPoIDkXxFff4/VmDAuG/Fxpdiwvh7lZUewr/EoqqurYTQaMXPmTLi7u1OgAkRERCA9PR3Xrl1DZ2cnLly4gMLCQkycOBFatQTDBkhALXyZiTVSq+b3BQAFu4iy3/LCUNkca5259d36iAFCc/ni2le1XAlKJXYcoX3aVvurFjl3rBgtddtwfN9m7ChIQ+jIubcx/oBnXud0wPJlf0bWxkZsyW9GUeFB5NMr+7tkewv+dv5rdB/ffPMNysvLcfHiRdzp+O6775CdnU3EaoPnBktBMZ1wcRB53BWAj44fEtJFLvfLpEQ4k1mADGmWfTa7F8aW4uLZOtSWLUf5liQUbExA1prFWJeRgikTXvtV0P5DMynTZZTpOmwtOIDMN2uRsmwn4haVEy/swIqUKpw69QXOnz+PgoIClJSUoL6+HufOneOyfj/H1atXERsbC2d7C2yM1/1AajKiR6QwK6l1i3SnCFmIzM24hUet8oKzkwf6uzkQYQlNfSoWW3Isf2vwjCiDRxrh2W/1HTVEt5088QUqKytpKgg5k0i6wLa0tISfnx+io6ORmpqK0tJSAusUfv7559tAYO3B2sfTWYzcRP1V4gXXhwaAFFlSEY3HQNLpbCbHRqiYXjeVPS1IsNGbc8Awqczmu1LhTvO8Dzfn2VaoUfvAziacNMDy3wTA1ycdNTVtIN/w9ttvcwFdv34dHR0daGtro3M1yMnJQWJiIsLCwkhgucDb2xsxMTEoKirC5cuXTUCkpKQgcKAExGsNDwUAEWI/Cv6nbSu5QC/QNNhG3JFIS8tkKrFoOneNdgUYNEJuXrMRyRaasbQtjhsmQwj1pEdvElXCrgphgqqvc/JdKyE+rhw3bty4r7L//vvv0dDQgMzMTISEhHCTIi8vD1euXIGXlyeyE3SdTXvLHB4IABqRFiRBt1DAyVRKzndakWlc/kD9xogH224hQ7JOsn+SXWHv2XkiWhN/3Clwa6sXbk6HVVyGMzIy0NzcjF20d1RVVaGurg6tra1ob2+/Iy/88ssvXGuEh4fD09MTwcHB3FLVdqQi6IEAYGPyXq3RPUI3xOnOsCc8tLi8SOTaXykXSG7ZKtWzw5Rr501QdgkoEkAGfTD32hX8Ck5TMHXJNkJrqzEgcQYCHV4uFpwy9XEXw5PkeR9bESy1xA9sEkklMBgM0Ol0VFky2lYFsCKQ2XfYNHiGXhnwR5rLvXr8kZijjagPBf0xBZZBjg2617ZGTg2cPU5pIkum+52d4khLxECl9OSCZ0+LLA3BGPqMjJXu34l4PyE7mjJdfYCZMVZ7hP4+S4LtaxJsP7GRnDZPwx6idL45X3stPUbbTsqwkXxaR8l5IylK/d6mJH17ZUWJqMcBoKxq7lUhtx7EHQVUIZg3UQl72itYG6hV3rTtiUCV8eOaBdp/sQUsYZrq3wsmK7OpvST3+k2qEnPyQ0zV9pue0Dlxjz4TfJCDPfQkwnw5LEA2i5GpjUHDPSMc5Stl6m03E2GkMneS7M6iyur7RD8Ufci26cU4w97SAgPdxGyZqX2YBx3/cwCwlqGl5Uu2wFDP/ni3De7/EgB2EA8Y2VNlIrWdf9Q9nygAiLhUGTHa/cQJ0x8rAJcuXTJLS0t7LJa1Pl2+KTtD+0fd7/Tp0/w/R3kAeAB4AHgAeAB4AHgAeAB4AHgAeAB4AHgAeAB4AJ5O+y882bsGMF+fXQAAAABJRU5ErkJggg==");
    }
}
