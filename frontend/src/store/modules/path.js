import PathService from '@/api/modules/path'

const state = {
  pathResult: null
}

const getters = {
  pathResult(state) {
    return state.pathResult
  }
}

const mutations = {
  setPath(state, pathResult) {
    state.pathResult = pathResult
  }
}

const actions = {
  async searchPath({commit}, {source, target, type, time}) {
    return PathService.get(source, target, type, time)
      .then(({data}) => {
        commit('setPath', data)
      })
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
