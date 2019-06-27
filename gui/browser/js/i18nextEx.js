// 国際化対応.
function i18nx(key, fallback, param1, param2) {
    i18n.init(MSF.Conf.System.Lang);
    var keyword = key;
    var spacer = "";

    // 前方空白文字(&nbsp;)でインデント調整しているキーワード対応：前方空白とキーワードを分割
    var matchStr = key.match(/((&nbsp;)+)(.*)/);
    if (matchStr !== null) {
        keyword = matchStr[matchStr.length - 1];
        spacer = key.slice(0, -1 * keyword.length);
    }
    // 国際化リソース(もしくはfallback)を返す
    if (i18n.exists(keyword)) {
        return spacer + i18n.t(keyword, param1, param2);
    } else {
        return fallback;
    }
}
