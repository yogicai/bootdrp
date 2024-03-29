let prefix = "/workbench";
let tableGrid;
//订单趋势图表的配置项和数据
let option1 = {
    title:{
        text:'订单趋势图',
    },
    grid: {
        left: 60,
        right: 50,
        bottom: 40
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            crossStyle: {
                color: '#999'
            }
        }
    },
    toolbox: {
        right: '5px',
        feature: {
            dataView: {show: false, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore: {show: true},
            saveAsImage: {show: false},
            myToolD: {
                show: true,
                title: '过去30天',
                icon: 'path://M664.644 466.559c0-23.155-3.473-43.994-10.42-63.1a166.564 166.564 0 0 0-27.786-48.625 150.268 150.268 0 0 0-43.994-31.838 143.4 143.4 0 0 0-54.993-11c-37.048 0-75.253 13.893-114.617 40.521a73.171 73.171 0 0 1-1.734-12.152c-0.579-1.737-1.158-2.894-1.158-4.052-1.737-2.894-5.789-5.789-11.577-8.683-6.948-2.316-12.737-4.05-17.365-4.05-1.158 0-3.473 1.158-5.789 2.894a61.316 61.316 0 0 0-6.946 5.789 75.169 75.169 0 0 0-6.946 8.1c-1.74 2.317-2.319 4.054-2.319 5.212 0 3.473 0 6.368 1.158 8.683s2.315 5.21 3.473 8.1c1.737 3.473 3.473 6.368 4.631 9.262a71.993 71.993 0 0 0 2.315 9.262l1.158 6.947a43.813 43.813 0 0 0 0.579 8.683 60.091 60.091 0 0 0-4.052 6.946 17.846 17.846 0 0 0-0.579 5.789c0 5.21 2.315 10.42 7.525 15.63l2.315 26.628 1.158 26.628v26.049c0 8.1 0.579 15.63 0.579 21.418 0 18.524-0.579 34.732-0.579 48.625 0 15.051-0.579 28.944-0.579 41.679l-1.737 37.048-1.157 38.788a7.432 7.432 0 0 0 2.315 5.789 18.1 18.1 0 0 0 6.368 5.21 20.424 20.424 0 0 0 7.525 4.631 15.092 15.092 0 0 0 6.368 1.737c5.21 0 9.841-1.737 14.472-5.21 4.052-2.894 6.368-8.1 7.525-16.208 16.208 8.683 31.838 15.051 45.731 18.524 13.893 4.052 24.891 5.789 32.417 5.789 24.313 0 47.467-4.631 68.307-13.893 20.26-8.683 37.627-23.155 52.1-42.836s26.049-45.152 34.153-76.411c8.103-30.682 12.155-68.309 12.155-112.303z m-47.467 9.841c0 35.311-3.473 65.412-9.262 90.883-6.368 25.47-15.051 46.889-25.47 63.676s-23.155 28.944-37.048 37.048c-15.051 8.1-30.1 12.156-46.31 12.156-9.841 0-18.524-1.158-25.47-4.052a110.242 110.242 0 0 1-20.26-9.841 57.185 57.185 0 0 1-8.683-5.21l-6.946-5.21-6.948-3.47c-2.315-0.579-4.631-1.158-8.1-2.315V481.03l-1.158-35.311c0-11.577-0.579-23.734-0.579-35.89a33.091 33.091 0 0 1 4.052-4.631l3.473-4.631a20.6 20.6 0 0 1 6.946-7.525l10.42-10.42c2.894-2.894 5.789-5.789 9.262-8.683a171.366 171.366 0 0 1 18.524-13.893 82.129 82.129 0 0 1 20.839-8.1 180.29 180.29 0 0 1 28.365-2.315 72.868 72.868 0 0 1 38.206 10.42 107.993 107.993 0 0 1 29.522 28.944 154.9 154.9 0 0 1 19.682 40.521 158.53 158.53 0 0 1 6.943 46.883z',
                onclick: function (){
                    searchPBillTrend('DAY');
                }
            },
            myToolM: {
                show: true,
                title: '过去12月',
                icon: 'path://M694.782 700.791c0-1.18-0.59-2.36-1.18-4.13l-1.77-3.54a9.516 9.516 0 0 1-1.77-5.31 6.373 6.373 0 0 1-1.77-4.13 109.223 109.223 0 0 1-6.49-18.289c-1.77-6.49-2.95-14.749-3.54-25.369-1.18-11.8-2.36-24.779-2.36-37.758-1.18-12.979-1.18-25.369-1.18-37.168 0-25.959 0-48.968 1.18-69.027 0-20.059 1.18-38.938 2.36-56.637 0.59-16.519 1.77-32.448 3.54-47.2s4.13-29.5 6.49-44.248l2.36-10.029a51.955 51.955 0 0 0 1.18-10.03c-0.59-4.13-2.95-7.08-7.67-8.85-4.72-1.18-9.439-2.36-13.569-2.36-5.9 0-11.8 0.59-17.7 1.18-6.49 1.18-11.209 2.95-14.749 5.31-8.26 5.9-16.519 16.519-24.779 30.678a438.325 438.325 0 0 0-25.958 50.156c-8.85 19.469-17.109 40.118-24.779 61.357-8.26 22.419-16.519 43.658-23.6 63.717-8.26 21.239-14.749 38.938-20.059 54.277-5.9 15.339-11.8 28.319-17.109 40.118-10.618-16.524-18.878-31.269-25.96-44.842a354.045 354.045 0 0 1-18.876-40.118 411.024 411.024 0 0 1-24.779-73.156 778.19 778.19 0 0 1-15.929-77.286c-1.18-8.85-2.36-18.289-2.95-27.729-1.18-9.44-2.36-18.879-4.13-28.319-2.36-6.49-7.08-11.8-15.339-15.339-8.85-2.95-17.109-4.72-24.189-4.72-9.44 0-15.929 1.77-18.289 4.72a18.518 18.518 0 0 0-4.13 12.389v8.26c0 3.54-0.59 5.9-0.59 7.67-5.899 25.958-10.029 56.637-12.979 92.035-3.54 35.398-4.72 73.746-4.72 115.044v38.938l1.18 24.184c0 8.26 0 16.519 0.59 24.189a294.812 294.812 0 0 0 2.95 32.448c0.59 8.26 1.77 14.159 2.95 17.109 2.36 8.26 8.85 12.389 18.289 12.389 7.67 0 14.749-1.18 21.239-4.72 5.9-3.54 8.85-7.08 8.85-11.209a29.966 29.966 0 0 0-1.77-10.029c-1.18-2.95-1.77-5.9-2.95-9.44l-2.36-8.26a17.133 17.133 0 0 1-1.77-8.26c-2.36-12.389-3.54-30.678-4.72-55.457s-1.178-53.091-1.178-85.54v-27.728l1.18-23.6 2.36-23.6 3.54-26.549c2.36 12.979 4.72 24.189 7.08 34.218a700.482 700.482 0 0 0 7.08 28.909c2.36 9.44 4.72 18.879 7.08 27.139a216.168 216.168 0 0 0 8.85 25.369c10.03 28.322 21.825 56.049 35.984 82.007 12.979 26.549 28.319 48.968 46.018 66.667 4.72 5.9 10.619 8.26 17.7 8.26 8.26 0 15.929-4.13 22.419-12.389 2.36-2.36 4.72-7.08 8.26-14.159 2.95-7.08 5.9-14.749 9.44-23.009 2.95-8.26 5.9-15.929 8.85-24.189l6.49-17.7 16.516-46.018c4.72-15.339 10.619-31.268 17.109-47.788 5.9-16.519 12.979-33.628 21.239-51.917 7.67-17.7 16.519-37.168 27.139-57.817-2.36 18.879-4.13 37.168-5.31 56.047-1.77 19.469-2.95 38.348-3.54 57.227-1.184 18.88-1.184 37.169-1.184 54.868-0.59 18.289-0.59 35.4-0.59 50.737 0 18.879 0.59 35.4 2.95 50.737s5.31 27.729 8.85 37.168c1.77 5.9 5.31 10.619 11.209 14.749a28.55 28.55 0 0 0 17.109 5.9c5.9 0 11.209-1.18 14.749-3.54s5.905-4.719 5.905-7.668z',
                onclick: function (){
                    searchPBillTrend('MONTH');
                }
            },
            myToolY: {
                show: true,
                title: '过去5年',
                icon: 'path://MM647.761 332.294c0-4.806-2.67-9.613-8.011-13.885C633.876 314.136 628 312 622.661 312c-4.272 0-8.545 1.6-12.817 4.272-4.806 3.2-8.011 5.874-10.147 9.079a139.029 139.029 0 0 0-8.011 14.953l-6.409 16.021-6.409 17.089a138.364 138.364 0 0 1-7.477 15.487l-32.041 59.281c-11.749 20.294-23.5 41.656-36.315 65.154l-14.954-22.965c-4.272-5.875-8.011-11.749-11.749-17.624a241.585 241.585 0 0 1-19.225-36.847c-6.409-16.021-11.749-32.043-14.953-48.6-4.272-16.021-7.477-32.043-10.681-48.6a24.163 24.163 0 0 0-9.613-13.351 33.13 33.13 0 0 0-17.089-4.806 36.962 36.962 0 0 0-18.158 4.806Q377 330.959 377 338.168a9.022 9.022 0 0 0 1.6 5.34c1.068 2.136 1.6 4.272 2.67 5.875 1.068 2.136 2.136 4.806 3.2 6.943s2.136 4.806 3.2 6.943a346.625 346.625 0 0 0 16.021 51.8A443.33 443.33 0 0 0 424 461a472.847 472.847 0 0 0 25.63 44.324c9.079 14.953 19.76 30.975 31.509 47-5.874 9.613-11.215 18.158-15.487 26.168-5.341 8.541-10.152 17.086-14.952 25.097-2.136 3.738-5.874 9.079-10.681 16.021L425.6 642.04a413.509 413.509 0 0 0-13.351 21.362c-4.272 6.409-6.943 11.215-7.477 13.351a18.792 18.792 0 0 0-1.068 6.409c0 6.943 2.136 13.351 6.943 19.76A20.837 20.837 0 0 0 428.268 712c6.409 0 11.215-2.67 14.419-7.477A150.417 150.417 0 0 0 453.9 684.23c3.2-6.409 6.943-13.351 11.215-20.294 10.685-19.226 21.364-38.451 32.579-57.677 10.681-18.692 20.828-37.917 31.506-58.211 16.555-32.043 33.645-63.551 51.8-94.526s35.247-63.017 51.268-95.594a13.385 13.385 0 0 1 2.136-3.738 30.63 30.63 0 0 1 2.136-3.738 16.229 16.229 0 0 1 2.67-3.738 22.611 22.611 0 0 0 2.67-3.738 34.059 34.059 0 0 0 3.2-4.806c1.613-2.67 2.681-4.808 2.681-5.876z',
                onclick: function (){
                    searchPBillTrend('YEAR');
                }
            }
        }
    },
    legend: {
        data:['订单金额','订单欠款','订单数']
    },
    xAxis: [
        {
            type: 'category',
            data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
            axisPointer: {
                type: 'shadow'
            }
        }
    ],
    yAxis: [
        {
            type: 'value',
            min: 0,
            max: 250,
            interval: 50
        },
        {
            type: 'value',
            min: 0,
            max: 25,
            interval: 5
        }
    ],
    series: [
        {
            name:'订单金额',
            type:'bar',
            data:[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
        },
        {
            name:'订单欠款',
            type:'bar',
            data:[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
        },
        {
            name:'订单数',
            type:'line',
            yAxisIndex: 1,
            data:[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
        }
    ]
};
//营业额利润饼图的配置项和数据
let option2 = {
    tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b}: {c} ({d}%)"
    },
    toolbox: {
        right: '5px',
        feature: {
            restore: {show: false},
            saveAsImage: {show: false},
            myToolD: {
                show: true,
                title: '过去30天',
                icon: 'path://M664.644 466.559c0-23.155-3.473-43.994-10.42-63.1a166.564 166.564 0 0 0-27.786-48.625 150.268 150.268 0 0 0-43.994-31.838 143.4 143.4 0 0 0-54.993-11c-37.048 0-75.253 13.893-114.617 40.521a73.171 73.171 0 0 1-1.734-12.152c-0.579-1.737-1.158-2.894-1.158-4.052-1.737-2.894-5.789-5.789-11.577-8.683-6.948-2.316-12.737-4.05-17.365-4.05-1.158 0-3.473 1.158-5.789 2.894a61.316 61.316 0 0 0-6.946 5.789 75.169 75.169 0 0 0-6.946 8.1c-1.74 2.317-2.319 4.054-2.319 5.212 0 3.473 0 6.368 1.158 8.683s2.315 5.21 3.473 8.1c1.737 3.473 3.473 6.368 4.631 9.262a71.993 71.993 0 0 0 2.315 9.262l1.158 6.947a43.813 43.813 0 0 0 0.579 8.683 60.091 60.091 0 0 0-4.052 6.946 17.846 17.846 0 0 0-0.579 5.789c0 5.21 2.315 10.42 7.525 15.63l2.315 26.628 1.158 26.628v26.049c0 8.1 0.579 15.63 0.579 21.418 0 18.524-0.579 34.732-0.579 48.625 0 15.051-0.579 28.944-0.579 41.679l-1.737 37.048-1.157 38.788a7.432 7.432 0 0 0 2.315 5.789 18.1 18.1 0 0 0 6.368 5.21 20.424 20.424 0 0 0 7.525 4.631 15.092 15.092 0 0 0 6.368 1.737c5.21 0 9.841-1.737 14.472-5.21 4.052-2.894 6.368-8.1 7.525-16.208 16.208 8.683 31.838 15.051 45.731 18.524 13.893 4.052 24.891 5.789 32.417 5.789 24.313 0 47.467-4.631 68.307-13.893 20.26-8.683 37.627-23.155 52.1-42.836s26.049-45.152 34.153-76.411c8.103-30.682 12.155-68.309 12.155-112.303z m-47.467 9.841c0 35.311-3.473 65.412-9.262 90.883-6.368 25.47-15.051 46.889-25.47 63.676s-23.155 28.944-37.048 37.048c-15.051 8.1-30.1 12.156-46.31 12.156-9.841 0-18.524-1.158-25.47-4.052a110.242 110.242 0 0 1-20.26-9.841 57.185 57.185 0 0 1-8.683-5.21l-6.946-5.21-6.948-3.47c-2.315-0.579-4.631-1.158-8.1-2.315V481.03l-1.158-35.311c0-11.577-0.579-23.734-0.579-35.89a33.091 33.091 0 0 1 4.052-4.631l3.473-4.631a20.6 20.6 0 0 1 6.946-7.525l10.42-10.42c2.894-2.894 5.789-5.789 9.262-8.683a171.366 171.366 0 0 1 18.524-13.893 82.129 82.129 0 0 1 20.839-8.1 180.29 180.29 0 0 1 28.365-2.315 72.868 72.868 0 0 1 38.206 10.42 107.993 107.993 0 0 1 29.522 28.944 154.9 154.9 0 0 1 19.682 40.521 158.53 158.53 0 0 1 6.943 46.883z',
                onclick: function (){
                    searchPBillTrendPie('DAY');
                }
            },
            myToolM: {
                show: true,
                title: '过去12月',
                icon: 'path://M694.782 700.791c0-1.18-0.59-2.36-1.18-4.13l-1.77-3.54a9.516 9.516 0 0 1-1.77-5.31 6.373 6.373 0 0 1-1.77-4.13 109.223 109.223 0 0 1-6.49-18.289c-1.77-6.49-2.95-14.749-3.54-25.369-1.18-11.8-2.36-24.779-2.36-37.758-1.18-12.979-1.18-25.369-1.18-37.168 0-25.959 0-48.968 1.18-69.027 0-20.059 1.18-38.938 2.36-56.637 0.59-16.519 1.77-32.448 3.54-47.2s4.13-29.5 6.49-44.248l2.36-10.029a51.955 51.955 0 0 0 1.18-10.03c-0.59-4.13-2.95-7.08-7.67-8.85-4.72-1.18-9.439-2.36-13.569-2.36-5.9 0-11.8 0.59-17.7 1.18-6.49 1.18-11.209 2.95-14.749 5.31-8.26 5.9-16.519 16.519-24.779 30.678a438.325 438.325 0 0 0-25.958 50.156c-8.85 19.469-17.109 40.118-24.779 61.357-8.26 22.419-16.519 43.658-23.6 63.717-8.26 21.239-14.749 38.938-20.059 54.277-5.9 15.339-11.8 28.319-17.109 40.118-10.618-16.524-18.878-31.269-25.96-44.842a354.045 354.045 0 0 1-18.876-40.118 411.024 411.024 0 0 1-24.779-73.156 778.19 778.19 0 0 1-15.929-77.286c-1.18-8.85-2.36-18.289-2.95-27.729-1.18-9.44-2.36-18.879-4.13-28.319-2.36-6.49-7.08-11.8-15.339-15.339-8.85-2.95-17.109-4.72-24.189-4.72-9.44 0-15.929 1.77-18.289 4.72a18.518 18.518 0 0 0-4.13 12.389v8.26c0 3.54-0.59 5.9-0.59 7.67-5.899 25.958-10.029 56.637-12.979 92.035-3.54 35.398-4.72 73.746-4.72 115.044v38.938l1.18 24.184c0 8.26 0 16.519 0.59 24.189a294.812 294.812 0 0 0 2.95 32.448c0.59 8.26 1.77 14.159 2.95 17.109 2.36 8.26 8.85 12.389 18.289 12.389 7.67 0 14.749-1.18 21.239-4.72 5.9-3.54 8.85-7.08 8.85-11.209a29.966 29.966 0 0 0-1.77-10.029c-1.18-2.95-1.77-5.9-2.95-9.44l-2.36-8.26a17.133 17.133 0 0 1-1.77-8.26c-2.36-12.389-3.54-30.678-4.72-55.457s-1.178-53.091-1.178-85.54v-27.728l1.18-23.6 2.36-23.6 3.54-26.549c2.36 12.979 4.72 24.189 7.08 34.218a700.482 700.482 0 0 0 7.08 28.909c2.36 9.44 4.72 18.879 7.08 27.139a216.168 216.168 0 0 0 8.85 25.369c10.03 28.322 21.825 56.049 35.984 82.007 12.979 26.549 28.319 48.968 46.018 66.667 4.72 5.9 10.619 8.26 17.7 8.26 8.26 0 15.929-4.13 22.419-12.389 2.36-2.36 4.72-7.08 8.26-14.159 2.95-7.08 5.9-14.749 9.44-23.009 2.95-8.26 5.9-15.929 8.85-24.189l6.49-17.7 16.516-46.018c4.72-15.339 10.619-31.268 17.109-47.788 5.9-16.519 12.979-33.628 21.239-51.917 7.67-17.7 16.519-37.168 27.139-57.817-2.36 18.879-4.13 37.168-5.31 56.047-1.77 19.469-2.95 38.348-3.54 57.227-1.184 18.88-1.184 37.169-1.184 54.868-0.59 18.289-0.59 35.4-0.59 50.737 0 18.879 0.59 35.4 2.95 50.737s5.31 27.729 8.85 37.168c1.77 5.9 5.31 10.619 11.209 14.749a28.55 28.55 0 0 0 17.109 5.9c5.9 0 11.209-1.18 14.749-3.54s5.905-4.719 5.905-7.668z',
                onclick: function (){
                    searchPBillTrendPie('MONTH');
                }
            },
            myToolY: {
                show: true,
                title: '过去5年',
                icon: 'path://MM647.761 332.294c0-4.806-2.67-9.613-8.011-13.885C633.876 314.136 628 312 622.661 312c-4.272 0-8.545 1.6-12.817 4.272-4.806 3.2-8.011 5.874-10.147 9.079a139.029 139.029 0 0 0-8.011 14.953l-6.409 16.021-6.409 17.089a138.364 138.364 0 0 1-7.477 15.487l-32.041 59.281c-11.749 20.294-23.5 41.656-36.315 65.154l-14.954-22.965c-4.272-5.875-8.011-11.749-11.749-17.624a241.585 241.585 0 0 1-19.225-36.847c-6.409-16.021-11.749-32.043-14.953-48.6-4.272-16.021-7.477-32.043-10.681-48.6a24.163 24.163 0 0 0-9.613-13.351 33.13 33.13 0 0 0-17.089-4.806 36.962 36.962 0 0 0-18.158 4.806Q377 330.959 377 338.168a9.022 9.022 0 0 0 1.6 5.34c1.068 2.136 1.6 4.272 2.67 5.875 1.068 2.136 2.136 4.806 3.2 6.943s2.136 4.806 3.2 6.943a346.625 346.625 0 0 0 16.021 51.8A443.33 443.33 0 0 0 424 461a472.847 472.847 0 0 0 25.63 44.324c9.079 14.953 19.76 30.975 31.509 47-5.874 9.613-11.215 18.158-15.487 26.168-5.341 8.541-10.152 17.086-14.952 25.097-2.136 3.738-5.874 9.079-10.681 16.021L425.6 642.04a413.509 413.509 0 0 0-13.351 21.362c-4.272 6.409-6.943 11.215-7.477 13.351a18.792 18.792 0 0 0-1.068 6.409c0 6.943 2.136 13.351 6.943 19.76A20.837 20.837 0 0 0 428.268 712c6.409 0 11.215-2.67 14.419-7.477A150.417 150.417 0 0 0 453.9 684.23c3.2-6.409 6.943-13.351 11.215-20.294 10.685-19.226 21.364-38.451 32.579-57.677 10.681-18.692 20.828-37.917 31.506-58.211 16.555-32.043 33.645-63.551 51.8-94.526s35.247-63.017 51.268-95.594a13.385 13.385 0 0 1 2.136-3.738 30.63 30.63 0 0 1 2.136-3.738 16.229 16.229 0 0 1 2.67-3.738 22.611 22.611 0 0 0 2.67-3.738 34.059 34.059 0 0 0 3.2-4.806c1.613-2.67 2.681-4.808 2.681-5.876z',
                onclick: function (){
                    searchPBillTrendPie ('YEAR');
                }
            }
        }
    },
    legend: {
        orient: 'vertical',
        x: 'left',
        data:['','','','','','','','','','']
    },
    series: [
        {
            name:'营业额',
            type:'pie',
            radius: ['60%', '80%'],
            data:[
                {value:0, name:''},
                {value:0, name:''},
                {value:0, name:''},
                {value:0, name:''},
                {value:0, name:''},
                {value:0, name:''},
                {value:0, name:''},
                {value:0, name:''}
            ]
        },
        {
            name:'利润',
            type:'pie',
            selectedMode: 'single',
            radius: [0, '50%'],
            label: {
                // show: false,
                normal: {
                    position: 'inner'
                }
            },
            labelLine: {
                normal: {
                    show: false
                }
            },
            data:[
                {value:0, name:'', selected:false},
                {value:0, name:''},
                {value:0, name:''}
            ]
        }
    ]
};
//利润趋势图表的配置和数据
let option3 = {
    title : {
        text: '利润趋势图'
    },
    grid: {
        left: 60,
        right: 50,
        bottom: 40
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['毛利']
    },
    toolbox: {
        right: '5px',
        feature: {
            dataView: {show: false, readOnly: false},
            magicType: {show: true, type: ['line','bar']},
            restore: {show: false},
            saveAsImage: {show: false},
            myToolD: {
                show: true,
                title: '过去30天',
                icon: 'path://M664.644 466.559c0-23.155-3.473-43.994-10.42-63.1a166.564 166.564 0 0 0-27.786-48.625 150.268 150.268 0 0 0-43.994-31.838 143.4 143.4 0 0 0-54.993-11c-37.048 0-75.253 13.893-114.617 40.521a73.171 73.171 0 0 1-1.734-12.152c-0.579-1.737-1.158-2.894-1.158-4.052-1.737-2.894-5.789-5.789-11.577-8.683-6.948-2.316-12.737-4.05-17.365-4.05-1.158 0-3.473 1.158-5.789 2.894a61.316 61.316 0 0 0-6.946 5.789 75.169 75.169 0 0 0-6.946 8.1c-1.74 2.317-2.319 4.054-2.319 5.212 0 3.473 0 6.368 1.158 8.683s2.315 5.21 3.473 8.1c1.737 3.473 3.473 6.368 4.631 9.262a71.993 71.993 0 0 0 2.315 9.262l1.158 6.947a43.813 43.813 0 0 0 0.579 8.683 60.091 60.091 0 0 0-4.052 6.946 17.846 17.846 0 0 0-0.579 5.789c0 5.21 2.315 10.42 7.525 15.63l2.315 26.628 1.158 26.628v26.049c0 8.1 0.579 15.63 0.579 21.418 0 18.524-0.579 34.732-0.579 48.625 0 15.051-0.579 28.944-0.579 41.679l-1.737 37.048-1.157 38.788a7.432 7.432 0 0 0 2.315 5.789 18.1 18.1 0 0 0 6.368 5.21 20.424 20.424 0 0 0 7.525 4.631 15.092 15.092 0 0 0 6.368 1.737c5.21 0 9.841-1.737 14.472-5.21 4.052-2.894 6.368-8.1 7.525-16.208 16.208 8.683 31.838 15.051 45.731 18.524 13.893 4.052 24.891 5.789 32.417 5.789 24.313 0 47.467-4.631 68.307-13.893 20.26-8.683 37.627-23.155 52.1-42.836s26.049-45.152 34.153-76.411c8.103-30.682 12.155-68.309 12.155-112.303z m-47.467 9.841c0 35.311-3.473 65.412-9.262 90.883-6.368 25.47-15.051 46.889-25.47 63.676s-23.155 28.944-37.048 37.048c-15.051 8.1-30.1 12.156-46.31 12.156-9.841 0-18.524-1.158-25.47-4.052a110.242 110.242 0 0 1-20.26-9.841 57.185 57.185 0 0 1-8.683-5.21l-6.946-5.21-6.948-3.47c-2.315-0.579-4.631-1.158-8.1-2.315V481.03l-1.158-35.311c0-11.577-0.579-23.734-0.579-35.89a33.091 33.091 0 0 1 4.052-4.631l3.473-4.631a20.6 20.6 0 0 1 6.946-7.525l10.42-10.42c2.894-2.894 5.789-5.789 9.262-8.683a171.366 171.366 0 0 1 18.524-13.893 82.129 82.129 0 0 1 20.839-8.1 180.29 180.29 0 0 1 28.365-2.315 72.868 72.868 0 0 1 38.206 10.42 107.993 107.993 0 0 1 29.522 28.944 154.9 154.9 0 0 1 19.682 40.521 158.53 158.53 0 0 1 6.943 46.883z',
                onclick: function (){
                    searchPCashTrend('DAY');
                }
            },
            myToolM: {
                show: true,
                title: '过去12月',
                icon: 'path://M694.782 700.791c0-1.18-0.59-2.36-1.18-4.13l-1.77-3.54a9.516 9.516 0 0 1-1.77-5.31 6.373 6.373 0 0 1-1.77-4.13 109.223 109.223 0 0 1-6.49-18.289c-1.77-6.49-2.95-14.749-3.54-25.369-1.18-11.8-2.36-24.779-2.36-37.758-1.18-12.979-1.18-25.369-1.18-37.168 0-25.959 0-48.968 1.18-69.027 0-20.059 1.18-38.938 2.36-56.637 0.59-16.519 1.77-32.448 3.54-47.2s4.13-29.5 6.49-44.248l2.36-10.029a51.955 51.955 0 0 0 1.18-10.03c-0.59-4.13-2.95-7.08-7.67-8.85-4.72-1.18-9.439-2.36-13.569-2.36-5.9 0-11.8 0.59-17.7 1.18-6.49 1.18-11.209 2.95-14.749 5.31-8.26 5.9-16.519 16.519-24.779 30.678a438.325 438.325 0 0 0-25.958 50.156c-8.85 19.469-17.109 40.118-24.779 61.357-8.26 22.419-16.519 43.658-23.6 63.717-8.26 21.239-14.749 38.938-20.059 54.277-5.9 15.339-11.8 28.319-17.109 40.118-10.618-16.524-18.878-31.269-25.96-44.842a354.045 354.045 0 0 1-18.876-40.118 411.024 411.024 0 0 1-24.779-73.156 778.19 778.19 0 0 1-15.929-77.286c-1.18-8.85-2.36-18.289-2.95-27.729-1.18-9.44-2.36-18.879-4.13-28.319-2.36-6.49-7.08-11.8-15.339-15.339-8.85-2.95-17.109-4.72-24.189-4.72-9.44 0-15.929 1.77-18.289 4.72a18.518 18.518 0 0 0-4.13 12.389v8.26c0 3.54-0.59 5.9-0.59 7.67-5.899 25.958-10.029 56.637-12.979 92.035-3.54 35.398-4.72 73.746-4.72 115.044v38.938l1.18 24.184c0 8.26 0 16.519 0.59 24.189a294.812 294.812 0 0 0 2.95 32.448c0.59 8.26 1.77 14.159 2.95 17.109 2.36 8.26 8.85 12.389 18.289 12.389 7.67 0 14.749-1.18 21.239-4.72 5.9-3.54 8.85-7.08 8.85-11.209a29.966 29.966 0 0 0-1.77-10.029c-1.18-2.95-1.77-5.9-2.95-9.44l-2.36-8.26a17.133 17.133 0 0 1-1.77-8.26c-2.36-12.389-3.54-30.678-4.72-55.457s-1.178-53.091-1.178-85.54v-27.728l1.18-23.6 2.36-23.6 3.54-26.549c2.36 12.979 4.72 24.189 7.08 34.218a700.482 700.482 0 0 0 7.08 28.909c2.36 9.44 4.72 18.879 7.08 27.139a216.168 216.168 0 0 0 8.85 25.369c10.03 28.322 21.825 56.049 35.984 82.007 12.979 26.549 28.319 48.968 46.018 66.667 4.72 5.9 10.619 8.26 17.7 8.26 8.26 0 15.929-4.13 22.419-12.389 2.36-2.36 4.72-7.08 8.26-14.159 2.95-7.08 5.9-14.749 9.44-23.009 2.95-8.26 5.9-15.929 8.85-24.189l6.49-17.7 16.516-46.018c4.72-15.339 10.619-31.268 17.109-47.788 5.9-16.519 12.979-33.628 21.239-51.917 7.67-17.7 16.519-37.168 27.139-57.817-2.36 18.879-4.13 37.168-5.31 56.047-1.77 19.469-2.95 38.348-3.54 57.227-1.184 18.88-1.184 37.169-1.184 54.868-0.59 18.289-0.59 35.4-0.59 50.737 0 18.879 0.59 35.4 2.95 50.737s5.31 27.729 8.85 37.168c1.77 5.9 5.31 10.619 11.209 14.749a28.55 28.55 0 0 0 17.109 5.9c5.9 0 11.209-1.18 14.749-3.54s5.905-4.719 5.905-7.668z',
                onclick: function (){
                    searchPCashTrend('MONTH');
                }
            },
            myToolY: {
                show: true,
                title: '过去5年',
                icon: 'path://M647.761 332.294c0-4.806-2.67-9.613-8.011-13.885C633.876 314.136 628 312 622.661 312c-4.272 0-8.545 1.6-12.817 4.272-4.806 3.2-8.011 5.874-10.147 9.079a139.029 139.029 0 0 0-8.011 14.953l-6.409 16.021-6.409 17.089a138.364 138.364 0 0 1-7.477 15.487l-32.041 59.281c-11.749 20.294-23.5 41.656-36.315 65.154l-14.954-22.965c-4.272-5.875-8.011-11.749-11.749-17.624a241.585 241.585 0 0 1-19.225-36.847c-6.409-16.021-11.749-32.043-14.953-48.6-4.272-16.021-7.477-32.043-10.681-48.6a24.163 24.163 0 0 0-9.613-13.351 33.13 33.13 0 0 0-17.089-4.806 36.962 36.962 0 0 0-18.158 4.806Q377 330.959 377 338.168a9.022 9.022 0 0 0 1.6 5.34c1.068 2.136 1.6 4.272 2.67 5.875 1.068 2.136 2.136 4.806 3.2 6.943s2.136 4.806 3.2 6.943a346.625 346.625 0 0 0 16.021 51.8A443.33 443.33 0 0 0 424 461a472.847 472.847 0 0 0 25.63 44.324c9.079 14.953 19.76 30.975 31.509 47-5.874 9.613-11.215 18.158-15.487 26.168-5.341 8.541-10.152 17.086-14.952 25.097-2.136 3.738-5.874 9.079-10.681 16.021L425.6 642.04a413.509 413.509 0 0 0-13.351 21.362c-4.272 6.409-6.943 11.215-7.477 13.351a18.792 18.792 0 0 0-1.068 6.409c0 6.943 2.136 13.351 6.943 19.76A20.837 20.837 0 0 0 428.268 712c6.409 0 11.215-2.67 14.419-7.477A150.417 150.417 0 0 0 453.9 684.23c3.2-6.409 6.943-13.351 11.215-20.294 10.685-19.226 21.364-38.451 32.579-57.677 10.681-18.692 20.828-37.917 31.506-58.211 16.555-32.043 33.645-63.551 51.8-94.526s35.247-63.017 51.268-95.594a13.385 13.385 0 0 1 2.136-3.738 30.63 30.63 0 0 1 2.136-3.738 16.229 16.229 0 0 1 2.67-3.738 22.611 22.611 0 0 0 2.67-3.738 34.059 34.059 0 0 0 3.2-4.806c1.613-2.67 2.681-4.808 2.681-5.876z',
                onclick: function (){
                    searchPCashTrend('YEAR');
                }
            }
        }
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'毛利',
            type:'bar',
            barMaxWidth: 15,
            data:[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
            // markPoint : {
            //     data: [ {type: 'max', name: '最大值'}, {type: 'min', name: '最小值'} ]
            // },
            markLine : {
                data : [ {type : 'average', name: '平均值'} ]
            },
            itemStyle: {
                normal: {
                    color:'#2f4554' //#006633
                }
            }
        }
    ]
};
//历史订单趋势图表的配置项和数据
let option4 = {
    title:{
        text:'历史订单趋势图',
    },
    grid: {
        left: 60,
        right: 50,
        bottom: 40
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            crossStyle: {
                color: '#999'
            }
        }
    },
    toolbox: {
        right: '5px',
        feature: {
            dataView: {show: false, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore: {show: true},
            saveAsImage: {show: false},
            myToolS: {
                show: true,
                title: '营业额',
                icon: 'path://M474.116145 958.890321c-33.739349 0-67.852216 2.916504-101.044081-1.345685-14.592755-1.872703-28.162175-17.386459-40.73896-28.377075-99.263479-86.78391-198.189257-173.956688-297.150852-261.083416-3.556089-3.131405-6.436776-7.03031-13.67687-15.048139 336.50831 0 667.260361 0 999.823715 0 0 17.166442 0 36.210703 0 59.701354-268.251876 0-536.575386 0-804.904012 0-1.038685 2.629971-2.07737 5.259941-3.110938 7.895028 87.633278 77.195263 175.266557 154.385409 262.899835 231.580673C475.512997 954.443931 474.817129 956.664568 474.116145 958.890321z" p-id="4022" fill="#cdcdcd"></path><path d="M10.898516 380.450307c0-18.363744 0-36.18512 0-58.064018 268.773777 0 537.342887 0 805.906881 0 1.401969-2.896038 2.803937-5.792075 4.205906-8.688113-87.883995-77.456213-175.767991-154.917544-263.657103-232.373757 0.562834-2.33832 1.125668-4.681757 1.688502-7.020077 43.128447 8.304362 88.53893-17.626942 130.244941 19.944796 92.340619 83.181772 186.59999 164.235007 280.040694 246.204127 13.047519 11.446 25.880138 23.137601 44.714615 40.002159C675.232139 380.450307 344.285654 380.450307 10.898516 380.450307z',
                onclick: function (){
                    searchHisPCashTrend('SALE');
                }
            },
            myToolP: {
                show: true,
                title: '毛利润',
                icon: 'path://M484.780654 954.007395c-23.22854 0-42.159288-19.033077-42.159289-42.159289V109.082043C442.621365 85.853503 461.552114 66.820426 484.780654 66.820426c23.22854 0 42.159288 19.033077 42.159288 42.159288v802.766064c0 23.330868-18.930748 42.261617-42.159288 42.261617z" p-id="6093" fill="#dbdbdb"></path><path d="M798.621765 422.104527c-16.372539 16.474868-43.284901 16.474868-59.65744 0.102328L454.900769 138.859598c-16.474868-16.372539-16.474868-43.284901-0.102328-59.65744C471.273309 62.727291 498.083342 62.727291 514.558209 79.09983l283.961227 283.244929c16.474868 16.474868 16.474868 43.284901 0.102329 59.759768z',
                onclick: function (){
                    searchHisPCashTrend('PROFIT');
                }
            },
            myToolD: {
                show: true,
                title: '欠款',
                icon: 'path://M484.780654 66.820426c-23.22854 0-42.159288 19.033077-42.159289 42.159288v802.766064c0 23.22854 19.033077 42.159288 42.159289 42.159288 23.22854 0 42.159288-19.033077 42.159288-42.159288V109.082043c0-23.22854-18.930748-42.261617-42.159288-42.261617z" p-id="6287" fill="#cdcdcd"></path><path d="M798.621765 598.825622c-16.372539-16.474868-43.284901-16.474868-59.65744-0.102328L454.900769 881.968222c-16.474868 16.372539-16.474868 43.284901-0.102328 59.65744 16.372539 16.474868 43.284901 16.474868 59.65744 0.102328l283.961227-283.244928c16.577196-16.372539 16.577196-43.182572 0.204657-59.65744z',
                onclick: function (){
                    searchHisPCashTrend('DEBT');
                }
            },
            myToolC: {
                show: true,
                title: '订单数',
                icon: 'path://M892.112593 967.111111H131.792593c-20.29037 0-39.348148-7.86963-53.665186-22.281481C63.81037 930.512593 55.940741 911.454815 55.940741 891.164444l0.663703-758.423703c0-20.29037 7.964444-39.348148 22.281482-53.665185C93.202963 64.758519 112.260741 56.888889 132.456296 56.888889h0.094815l630.613333 0.663704c15.739259 0 28.444444 12.8 28.444445 28.444444 0 15.739259-12.705185 28.444444-28.444445 28.444444l-630.613333-0.663703c-5.12 0-9.860741 1.991111-13.368889 5.499259s-5.594074 8.343704-5.594074 13.368889L112.82963 891.259259c0 5.025185 1.991111 9.860741 5.594074 13.463704 3.602963 3.602963 8.343704 5.594074 13.368889 5.594074h760.32c5.025185 0 9.860741-1.991111 13.368888-5.594074 3.602963-3.602963 5.594074-8.343704 5.594075-13.463704l-0.663704-619.899259c0-15.739259 12.705185-28.444444 28.444444-28.444444s28.444444 12.705185 28.444445 28.444444l0.663703 619.899259c0 20.29037-7.86963 39.348148-22.186666 53.665185-14.317037 14.317037-33.374815 22.186667-53.665185 22.186667z',
                onclick: function (){
                    searchHisPCashTrend('COUNT');
                }
            }
        }
    },
    legend: {
        data:['订单金额']
    },
    xAxis: [
        {
            type: 'category',
            data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
            axisPointer: {
                type: 'shadow'
            }
        }
    ],
    yAxis: [
        {
            type: 'value',
            min: 0,
            max: 250,
            interval: 50
        }
    ],
    series: [
        {
            name:'订单金额',
            type:'line',
            data:[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
        }
    ]
};


$(function() {

    let urlSearchParams = new URLSearchParams(window.location.search);
    let shopNo = urlSearchParams.get('shopNo');

    searchPBalance(shopNo);
    searchPSeTotal('MONTH', shopNo);
    searchPDebtTotal(shopNo);
    searchPBillTrend('DAY', shopNo);
    searchPBillTrendPie('DAY', shopNo);
    searchPCashTotal(shopNo);
    searchPCashTrend("DAY", shopNo);
    //营业额
    searchHisPCashTrend("SALE", shopNo);

});

/**
 * 订单趋势图
 */
function searchPBillTrend(type, shopNo) {
    // 基于准备好的dom，初始化echarts实例
    let myChart = echarts.init(document.getElementById('bill-bar-chart'));
    let _option = $.extend({}, option1);
    $.ajax({
        url : prefix+"/pBillTrend",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({type: type, shopNo: shopNo}),
        success : function(r) {
            if (r.code === 0) {
                _option.xAxis[0].data = r.result.xAxis[0].data;
                _option.yAxis[0] = r.result.yAxis[0];
                _option.yAxis[1] = r.result.yAxis[1];
                _option.series[0].data = r.result.series[0].data;
                _option.series[1].data = r.result.series[1].data;
                _option.series[2].data = r.result.series[2].data;
            }
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(_option);
        }
    });
}

/**
 * 利润趋势饼图
 */
function searchPBillTrendPie(type, shopNo) {
    // 基于准备好的dom，初始化echarts实例
    let myChart = echarts.init(document.getElementById('bill-pie-chart'));
    let _option = $.extend({}, option2);
    $.ajax({
        url : prefix+"/pBillTrendPie",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({type: type, shopNo: shopNo}),
        success : function(r) {
            if (r.code === 0) {
                _option.legend.data = r.result.legend.data;
                _option.series[0].data = r.result.series[0].data;
                _option.series[1].data = r.result.series[1].data;
            }
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(_option);
        }
    });
}

/**
 * 利润趋势图
 */
function searchPCashTrend(type, shopNo) {
    // 基于准备好的dom，初始化echarts实例
    let myChart = echarts.init(document.getElementById('cash-bar-chart'));
    let _option = $.extend({}, option3);
    $.ajax({
        url : prefix+"/pCashTrend",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({type: type, shopNo: shopNo}),
        success : function(r) {
            if (r.code === 0) {
                _option.xAxis[0].data = r.result.xAxis[0].data;
                _option.yAxis[0] = r.result.yAxis[0];
                _option.series[0].data = r.result.series[0].data;
            }
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(_option);
        }
    });
}


/**
 * 订单同比趋势图
 */
function searchHisPCashTrend(type, shopNo) {

    let myChart = echarts.init(document.getElementById('bill-bar-chart-his'));
    let _option = $.extend({}, option4);
    $.ajax({
        url : prefix+"/pHisCashTrend",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({type: type, shopNo: shopNo}),
        success : function(r) {
            if (r.code === 0) {
                _option.title = r.result.title;
                _option.xAxis[0].data = r.result.xAxis[0].data;
                _option.yAxis[0] = r.result.yAxis[0];
                _option.series = r.result.series;
                _option.legend = r.result.legend;
            }
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(_option);
        }
    });
}

function animateNUM(element, value) {
    // how many decimal places allows
    let decimal_places = 2;
    let decimal_factor = decimal_places === 0 ? 1 : Math.pow(10, decimal_places);
    let _sign = value < 0 ? "-" : "";
    let _value = Math.abs(value);
    $('#'+element).animateNumber(
            {
                number: (_value * decimal_factor).toFixed(2),
                numberStep: function(now, tween) {
                    let floored_number = Math.floor(now) / decimal_factor,
                        target = $(tween.elem);
                    if (decimal_places > 0) {
                        // force decimal places even if they are 0
                        floored_number = floored_number.toFixed(decimal_places);
                    }
                    if (_value <= floored_number) {
                        target.text(_sign + thousandBitSeparator(_value));
                    } else {
                        target.text(_sign + floored_number);
                    }

                }
            },
            1000
        );
}

function thousandBitSeparator(num) {
    return num && num.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,');
}

function searchPBalance(shopNo) {
    $.ajax({
        url : prefix+"/pBalanceTotal",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({shopNo: shopNo}),
        success : function(r) {
            if (r.code === 0) {
                animateNUM('qtyTotalWH', r.result.qtyTotal);
                animateNUM('totalAmountWH', r.result.totalAmount);
            }
        }
    });
}

function searchPSeTotal(type, shopNo) {
    $.ajax({
        url : prefix+"/pSeTotal",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({type: type, shopNo: shopNo}),
        success : function(r) {
            if (type === 'WEEK') {
                animateNUM('totalAmountSEW', r.result.totalAmount);
                animateNUM('profitSEW', r.result.profit);
            } else {
                animateNUM('totalAmountSEM', r.result.totalAmount);
                animateNUM('profitSEM', r.result.profit);
            }
        }
    });
}

function searchPDebtTotal(shopNo) {
    $.ajax({
        url : prefix+"/pDebtTotal",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({shopNo: shopNo}),
        success : function(r) {
            if (r.code === 0) {
                animateNUM('debtAmountSE', r.result.debtAmount);
                animateNUM('debtVAmountSE', r.result.debtVAmount);
            }
        }
    });
}

function searchPCashTotal(shopNo) {
    $.ajax({
        url : prefix+"/pCashTotal",
        type : "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({shopNo: shopNo}),
        success : function(r) {
            if (r.code === 0) {
                animateNUM('profitAmountT', r.profitAmountT);
                animateNUM('cashFlowAmountT', r.cashFlowAmountT);
            }
        }
    });
}
